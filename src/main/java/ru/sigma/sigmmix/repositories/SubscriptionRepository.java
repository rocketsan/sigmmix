package ru.sigma.sigmmix.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.model.Subscription;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    @NotNull List<Subscription> findAll();

    List<Subscription> findByisActive(boolean b);

    List<Subscription> findByisRemoved(boolean b);

    List<Subscription> findByHostAndIsRemovedIsFalse(Host host);
}
