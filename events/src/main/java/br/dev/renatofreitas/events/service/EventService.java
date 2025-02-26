package br.dev.renatofreitas.events.service;

import br.dev.renatofreitas.events.model.Event;
import br.dev.renatofreitas.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event){ // Recebe algumas informações do evento e retorna o evento com as infos salvas e geradas no BD
        // gerando o pretty name
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents(){
        return (List<Event>) eventRepository.findAll(); // retorna os todos os eventos convertidos para uma lista de eventos
    }

    public Event getByPrettyName(String prettyName){
        return eventRepository.findByPrettyName(prettyName);
    }
}
