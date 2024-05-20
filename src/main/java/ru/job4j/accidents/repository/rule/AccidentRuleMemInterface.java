package ru.job4j.accidents.repository.rule;

import ru.job4j.accidents.model.AccidentRule;

import java.util.Collection;
import java.util.Optional;

public interface AccidentRuleMemInterface {
    AccidentRule save(AccidentRule rule);

    Optional<AccidentRule> findById(int id);

    Collection<AccidentRule> findAll();

    boolean update(AccidentRule rule);

    void deleteById(int id);

    Collection<AccidentRule> findByIds(Collection<Integer> ids);
}
