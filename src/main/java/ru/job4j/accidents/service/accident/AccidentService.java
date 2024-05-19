package ru.job4j.accidents.service.accident;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.AccidentMem;

import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentService implements AccidentServiceInterface {
    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
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
}
