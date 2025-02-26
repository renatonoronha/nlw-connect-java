package br.dev.renatofreitas.events.repository;

import br.dev.renatofreitas.events.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {
    public Event findByPrettyName(String prettyName);
}
