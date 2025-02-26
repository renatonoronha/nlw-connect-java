package br.dev.renatofreitas.events.controller;

import br.dev.renatofreitas.events.model.Event;
import br.dev.renatofreitas.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public Event addNewEvent(@RequestBody Event newEvent){
        return eventService.addNewEvent(newEvent);
    }

    @GetMapping("/events")
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName){
        Event event = eventService.getByPrettyName(prettyName);
        if (event != null) { // evento existe no banco de dados
            return ResponseEntity.ok().body(event);
        }
        return ResponseEntity.notFound().build();
    }

}
