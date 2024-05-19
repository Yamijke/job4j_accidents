package ru.job4j.accidents.repository.type;

import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;
import java.util.Optional;

public interface AccidentTypeMemInterface {
    AccidentType save(AccidentType type);

    Optional<AccidentType> findById(int id);

    Collection<AccidentType> findAll();

    boolean update(AccidentType type);

    void deleteById(int id);
}
