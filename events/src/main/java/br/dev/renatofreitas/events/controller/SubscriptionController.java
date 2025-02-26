package br.dev.renatofreitas.events.controller;

import br.dev.renatofreitas.events.dto.ErrorMessage;
import br.dev.renatofreitas.events.dto.SubscriptionResponse;
import br.dev.renatofreitas.events.exception.EventNotFoundException;
import br.dev.renatofreitas.events.exception.SubscriptionConflictException;
import br.dev.renatofreitas.events.exception.UserIndicadorNorFoundException;
import br.dev.renatofreitas.events.model.Subscription;
import br.dev.renatofreitas.events.model.User;
import br.dev.renatofreitas.events.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // A primeira URL é uma inscrição própria e a segunda URL é uma inscrição com indicação;
    // É possível retornar um Subscription ou um ErrorMessage no body da resposta, por isso é usado o "?".
    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName,
                                                @RequestBody User subscriber,
                                                @PathVariable(required = false) Integer userId){ // Não é obrigatório

        try {
            SubscriptionResponse result = subscriptionService.createNewSubscription(prettyName, subscriber, userId);

            // inscrição cadastrada com sucesso
            if (result != null){
                return ResponseEntity.ok(result);
            }

            // evento não existe
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));

            // duplicidade na inscrição
        } catch (SubscriptionConflictException e) {
            return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));

            // usuário indicador não existe
        } catch (UserIndicadorNorFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }

        // inscrição falhou
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName){

        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(prettyName).subList(0, 3));
        } catch (EventNotFoundException e){
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName, @PathVariable Integer userId){

        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
        } catch (Exception e) { // Lida com múltiplas exceções
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }
}
