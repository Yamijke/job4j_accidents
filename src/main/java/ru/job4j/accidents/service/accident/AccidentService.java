package ru.job4j.accidents.service.accident;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentRule;
import ru.job4j.accidents.repository.accident.AccidentMem;
import ru.job4j.accidents.service.rule.AccidentRuleService;

import java.util.*;

@Service
public class AccidentService implements AccidentServiceInterface {
    private final AccidentMem accidentMem;
    private final AccidentRuleService accidentRuleService;

    public AccidentService(AccidentMem accidentMem, AccidentRuleService accidentRuleService) {
        this.accidentMem = accidentMem;
        this.accidentRuleService = accidentRuleService;
    }

    @Override
    public Accident save(Accident accident) {
        return accidentMem.save(accident);
    }

    @Override
    public Optional<Accident> findById(int id) {
        return accidentMem.findById(id);
    }

    @Override
    public Collection<Accident> findAll() {
        return accidentMem.findAll();
    }

    @Override
    public boolean update(Accident accident) {
        return accidentMem.update(accident);
    }

    @Override
    public void deleteById(int id) {
        accidentMem.deleteById(id);
    }

    public Collection<AccidentRule> getRulesByIds(Collection<Integer> ruleIds) {
        return accidentMem.findRulesByIds(ruleIds);
    }
}
