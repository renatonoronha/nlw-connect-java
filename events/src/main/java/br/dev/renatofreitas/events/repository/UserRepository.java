package br.dev.renatofreitas.events.repository;

import br.dev.renatofreitas.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}
