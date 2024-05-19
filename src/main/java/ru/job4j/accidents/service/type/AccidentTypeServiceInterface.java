package ru.job4j.accidents.service.type;

import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;
import java.util.Optional;

public interface AccidentTypeServiceInterface {
    AccidentType save(AccidentType type);

    Optional<AccidentType> findById(int id);

    Collection<AccidentType> findAll();

    boolean update(AccidentType type);

    void deleteById(int id);
}
