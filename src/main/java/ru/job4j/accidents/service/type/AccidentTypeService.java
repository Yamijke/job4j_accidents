package ru.job4j.accidents.service.type;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.type.SpringDataAccidentTypeRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentTypeService implements AccidentTypeServiceInterface {
    private final SpringDataAccidentTypeRepository accidentTypeMem;

    public AccidentTypeService(SpringDataAccidentTypeRepository accidentTypeMem) {
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
        return (Collection<AccidentType>) accidentTypeMem.findAll();
    }

    @Override
    public void deleteById(int id) {
        accidentTypeMem.deleteById(id);
    }
}
