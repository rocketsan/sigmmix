package ru.sigma.sigmmix.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import ru.sigma.sigmmix.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String admin);

    @NotNull List<User> findAll();
}
