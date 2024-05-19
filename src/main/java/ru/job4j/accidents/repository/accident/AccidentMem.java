package ru.job4j.accidents.repository.accident;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.rule.AccidentRuleMem;
import ru.job4j.accidents.repository.type.AccidentTypeMem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements AccidentMemInterface {
    private int nextId = 1;
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    @Autowired
    public AccidentMem(AccidentTypeMem accidentTypeMem, AccidentRuleMem accidentRuleMem) {
        save(new Accident(0, "test", "test", "test", accidentTypeMem.findById(1).orElse(null), accidentRuleMem.findAll()));
        save(new Accident(0, "test1", "test1", "test1", accidentTypeMem.findById(2).orElse(null), accidentRuleMem.findAll()));
        save(new Accident(0, "test2", "test2", "test2", accidentTypeMem.findById(3).orElse(null), accidentRuleMem.findAll()));
    }

    @Override
    public Accident save(Accident accident) {
        accident.setId(nextId++);
        accidents.put(accident.getId(), accident);
        return accident;
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }

    @Override
    public Collection<Accident> findAll() {
        return accidents.values();
    }

    @Override
    public boolean update(Accident accident) {
        return accidents.computeIfPresent(accident.getId(),
                (id, oldAccident) -> new Accident(oldAccident.getId(),
                        accident.getName(),
                        accident.getText(),
                        accident.getAddress(),
                        accident.getType(),
                        accident.getRules())) != null;
    }

    @Override
    public void deleteById(int id) {
        accidents.remove(id);
    }
}
