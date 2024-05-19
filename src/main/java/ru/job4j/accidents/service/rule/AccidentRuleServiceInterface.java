package ru.job4j.accidents.service.rule;

import ru.job4j.accidents.model.AccidentRule;

import java.util.Collection;
import java.util.Optional;

public interface AccidentRuleServiceInterface {
    AccidentRule save(AccidentRule rule);

    Optional<AccidentRule> findById(int id);

    Collection<AccidentRule> findAll();

    boolean update(AccidentRule rule);

    void deleteById(int id);
}
