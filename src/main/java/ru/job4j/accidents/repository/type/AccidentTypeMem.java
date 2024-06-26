package ru.job4j.accidents.repository.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentTypeMem implements AccidentTypeMemInterface {

    private AtomicInteger nextId = new AtomicInteger(1);
    private Map<Integer, AccidentType> types = new ConcurrentHashMap<>();

    @Autowired
    public AccidentTypeMem() {
        save(new AccidentType(0, "Type1"));
        save(new AccidentType(0, "Type2"));
        save(new AccidentType(0, "Type3"));
    }

    @Override
    public AccidentType save(AccidentType type) {
        type.setId(nextId.getAndIncrement());
        types.put(type.getId(), type);
        return type;
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return Optional.ofNullable(types.get(id));
    }

    @Override
    public Collection<AccidentType> findAll() {
        return types.values();
    }

    @Override
    public boolean update(AccidentType type) {
        return types.computeIfPresent(type.getId(),
                (id, oldAccidentType) -> new AccidentType(oldAccidentType.getId(), type.getName())) != null;
    }

    @Override
    public void deleteById(int id) {
        types.remove(id);
    }
}
