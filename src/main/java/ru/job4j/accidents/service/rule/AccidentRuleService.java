package ru.job4j.accidents.service.rule;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentRule;
import ru.job4j.accidents.repository.rule.AccidentRuleMem;

import java.util.*;

@Service
public class AccidentRuleService implements AccidentRuleServiceInterface {
    private final AccidentRuleMem accidentRuleMem;

    public AccidentRuleService(AccidentRuleMem accidentRuleMem) {
        this.accidentRuleMem = accidentRuleMem;
    }

    @Override
    public AccidentRule save(AccidentRule rule) {
        return accidentRuleMem.save(rule);
    }

    @Override
    public Optional<AccidentRule> findById(int id) {
        return accidentRuleMem.findById(id);
    }

    @Override
    public Collection<AccidentRule> findAll() {
        return accidentRuleMem.findAll();
    }

    @Override
    public boolean update(AccidentRule rule) {
        return accidentRuleMem.update(rule);
    }

    @Override
    public void deleteById(int id) {
        accidentRuleMem.deleteById(id);
    }
}
