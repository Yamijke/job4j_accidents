package ru.job4j.accidents.repository.rule;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.AccidentRule;

import java.util.Collection;
import java.util.List;

public interface SpringDataAccidentRuleRepository extends CrudRepository<AccidentRule, Integer> {
    Collection<AccidentRule> findByIdIn(List<Integer> ids);
}
