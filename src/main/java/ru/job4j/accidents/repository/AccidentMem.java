package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements AccidentMemInterface {
    private static final AccidentMem INSTANCE = new AccidentMem();
    private int nextId = 1;
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private AccidentMem() {
        save(new Accident(0, "test", "test", "test"));
        save(new Accident(0, "test1", "test1", "test1"));
        save(new Accident(0, "test2", "test2", "test2"));
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
                (id, oldAccident) -> new Accident(oldAccident.getId(), accident.getName(), accident.getText(), accident.getAddress())) != null;
    }

    @Override
    public void deleteById(int id) {
        accidents.remove(id);
    }
}
