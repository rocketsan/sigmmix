package ru.sigma.sigmmix.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import ru.sigma.sigmmix.model.Template;

import java.util.List;

public interface TemplateRepository  extends CrudRepository<Template, Long> {

    @NotNull List<Template> findAll();

}
