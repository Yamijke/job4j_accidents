package ru.job4j.accidents.service.accident;

import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

public interface AccidentServiceInterface {
    Accident save(Accident accident);

    Optional<Accident> findById(int id);

    Collection<Accident> findAll();

    boolean update(Accident accident);

    void deleteById(int id);
}
