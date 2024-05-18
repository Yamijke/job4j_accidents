package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements AccidentMemInterface {
    private static final AccidentMem INSTANCE = new AccidentMem();
    private int nextId = 1;
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private AccidentMem() {
        Rule rule1 = new Rule(1, "Rule 1");
        Rule rule2 = new Rule(2, "Rule 2");
        Rule rule3 = new Rule(3, "Rule 3");

        Set<Rule> rules1 = new HashSet<>(Arrays.asList(rule1, rule2));
        Set<Rule> rules2 = new HashSet<>(Arrays.asList(rule2, rule3));
        Set<Rule> rules3 = new HashSet<>(Arrays.asList(rule1, rule3));

        save(new Accident(0, "test", "test", "test", new AccidentType(1, "Two cars"), rules1));
        save(new Accident(0, "test1", "test1", "test1", new AccidentType(2, "Car and human"), rules2));
        save(new Accident(0, "test2", "test2", "test2", new AccidentType(3, "Car and  велосипед"), rules3));
    }

    public static AccidentMem getInstance() {
        return INSTANCE;
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
                (id, oldAccident) -> new Accident(oldAccident.getId(), accident.getName(), accident.getText(), accident.getAddress(), accident.getType(), accident.getRules())) != null;
    }

    @Override
    public void deleteById(int id) {
        accidents.remove(id);
    }
}
