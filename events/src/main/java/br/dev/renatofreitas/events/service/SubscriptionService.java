package br.dev.renatofreitas.events.service;

import br.dev.renatofreitas.events.dto.SubscriptionRankingByUser;
import br.dev.renatofreitas.events.dto.SubscriptionRankingItem;
import br.dev.renatofreitas.events.dto.SubscriptionResponse;
import br.dev.renatofreitas.events.exception.EventNotFoundException;
import br.dev.renatofreitas.events.exception.SubscriptionConflictException;
import br.dev.renatofreitas.events.exception.UserIndicadorNorFoundException;
import br.dev.renatofreitas.events.model.Event;
import br.dev.renatofreitas.events.model.Subscription;
import br.dev.renatofreitas.events.model.User;
import br.dev.renatofreitas.events.repository.EventRepository;
import br.dev.renatofreitas.events.repository.SubscriptionRepository;
import br.dev.renatofreitas.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventPrettyName, User user, Integer userId){
        // recuperar o evento pelo nome
        Event event = eventRepository.findByPrettyName(eventPrettyName);

        //verificar se o evento não existe no banco de dados, se não, lançar uma exceção
        if (event == null){
            throw new EventNotFoundException("Evento " + eventPrettyName + " não existe");
        }

        // User Recovered (userRec), but User Retrieved (userRet) would be the right name in this context
        User userRec = userRepository.findByEmail(user.getEmail()); // procura se o User já está gravado no banco
        if (userRec == null){
            userRec = userRepository.save(user);
        }

        User indicador = null;
        if (userId != null){ // Se existir um userId, vai procurar no banco de dados o usuário que está relacionado a ele
            indicador = userRepository.findById(userId).orElse(null); // Se não existir usuário, então indicador é null
            if (indicador == null){
                throw new UserIndicadorNorFoundException("Usuário indicador " + userId + " não existe");
            }
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRec);
        subscription.setIndication(indicador);

        // verificar se já existe uma inscrição com esse usuário e esse evento, se sim, lançar uma exceção de conflito
        Subscription tempSubscription = subscriptionRepository.findByEventAndSubscriber(event, userRec);
        if (tempSubscription != null){
            throw new SubscriptionConflictException("O usuário " + userRec.getName() + " já está inscrito para o evento " + event.getTitle());
        }

        Subscription result = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(result.getSubscriptionNumber(),
                "http://codecraft.com/subscription/" + result.getEvent().getPrettyName() + "/" + result.getSubscriber().getId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName){
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null){
            throw new EventNotFoundException("Ranking do evento " + prettyName + " não existe");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId){
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        // stream -> transmissão de dados um por um, de um ponto A para o ponto B
        // para cada item da minha lista eu quero ver se o item, no seu userId, é igual ao userId que eu passei no parâmetro. Posso encontrar ou não
        SubscriptionRankingItem item = ranking.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);

        // usuário não está na lista (ranking)
        if (item == null){
            throw new UserIndicadorNorFoundException("Não há inscrições com indicação do usuário " + userId);
        }
        Integer posicao = IntStream.range(0, ranking.size())
                .filter(pos -> ranking.get(pos).userId().equals(userId))
                .findFirst().getAsInt();

        return new SubscriptionRankingByUser(item, posicao+1);
    }

}
