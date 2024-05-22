package ru.job4j.accidents.service.accident;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.SpringDataAccidentRepository;

import java.util.*;

@Service
public class AccidentService implements AccidentServiceInterface {
    private final SpringDataAccidentRepository accidentsRepository;

    public AccidentService(SpringDataAccidentRepository accidentsRepository1) {
        this.accidentsRepository = accidentsRepository1;
    }

    @Override
    public Accident save(Accident accident) {
        return accidentsRepository.save(accident);
    }

    @Override
    public Optional<Accident> findById(int id) {
        return accidentsRepository.findById(id);
    }

    @Override
    public Collection<Accident> findAll() {
        return (Collection<Accident>) accidentsRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        accidentsRepository.deleteById(id);
    }
}
