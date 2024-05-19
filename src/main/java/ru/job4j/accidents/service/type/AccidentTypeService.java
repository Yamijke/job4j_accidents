package ru.job4j.accidents.service.type;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.type.AccidentTypeMem;

import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentTypeService implements AccidentTypeServiceInterface {
    private final AccidentTypeMem accidentTypeMem;

    public AccidentTypeService(AccidentTypeMem accidentTypeMem) {
        this.accidentTypeMem = accidentTypeMem;
    }

    @Override
    public AccidentType save(AccidentType type) {
        return accidentTypeMem.save(type);
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return accidentTypeMem.findById(id);
    }

    @Override
    public Collection<AccidentType> findAll() {
        return accidentTypeMem.findAll();
    }

    @Override
    public boolean update(AccidentType type) {
        return accidentTypeMem.update(type);
    }

    @Override
    public void deleteById(int id) {
        accidentTypeMem.deleteById(id);
    }
}
