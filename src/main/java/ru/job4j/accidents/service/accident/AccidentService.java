package ru.job4j.accidents.service.accident;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.hbm.AccidentHibernate;

import java.util.*;

@Service
public class AccidentService implements AccidentServiceInterface {
    private final AccidentHibernate accidentsRepository;

    public AccidentService(AccidentHibernate accidentsRepository) {
        this.accidentsRepository = accidentsRepository;
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
        return accidentsRepository.findAll();
    }

    @Override
    public boolean update(Accident accident) {
        return accidentsRepository.update(accident);
    }

    @Override
    public void deleteById(int id) {
        accidentsRepository.deleteById(id);
    }
}
