package ru.job4j.accidents.repository.accident;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentRule;

import java.util.Collection;
import java.util.Optional;

public interface AccidentMemInterface {
    Accident save(Accident accident);

    Optional<Accident> findById(int id);

    Collection<Accident> findAll();

    boolean update(Accident accident);

    void deleteById(int id);

    Collection<AccidentRule> findRulesByIds(Collection<Integer> ruleIds);
}
