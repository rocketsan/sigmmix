package ru.sigma.sigmmix.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import ru.sigma.sigmmix.model.Host;

import java.util.List;

public interface HostRepository extends CrudRepository<Host, Long> {

    @NotNull List<Host> findAll();

    List<Host> findByisActive(boolean b);
}
