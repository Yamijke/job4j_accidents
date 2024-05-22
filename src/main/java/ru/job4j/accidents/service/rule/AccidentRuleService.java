package ru.job4j.accidents.service.rule;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentRule;
import ru.job4j.accidents.repository.rule.SpringDataAccidentRuleRepository;

import java.util.*;

@Service
public class AccidentRuleService implements AccidentRuleServiceInterface {
    private final SpringDataAccidentRuleRepository accidentRuleMem;

    public AccidentRuleService(SpringDataAccidentRuleRepository accidentRuleMem) {
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
        return (Collection<AccidentRule>) accidentRuleMem.findAll();
    }

    public Collection<AccidentRule> findByIds(List<Integer> ids) {
        return accidentRuleMem.findByIdIn(ids);
    }

    @Override
    public void deleteById(int id) {
        accidentRuleMem.deleteById(id);
    }
}
