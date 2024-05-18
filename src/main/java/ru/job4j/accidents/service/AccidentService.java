package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentJdbcTemplate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccidentService {
    private final AccidentJdbcTemplate accidentJdbcTemplate;

    public Accident save(Accident accident) {
        return accidentJdbcTemplate.save(accident);
    }

    public Optional<Accident> findById(int id) {
        return accidentJdbcTemplate.findById(id);
    }

    public List<Accident> findAll() {
        return accidentJdbcTemplate.findAll();
    }

    public boolean update(Accident accident) {
        return accidentJdbcTemplate.update(accident);
    }

    public void deleteById(int id) {
        accidentJdbcTemplate.deleteById(id);
    }

    public List<AccidentType> getAllAccidentTypes() {
        return accidentJdbcTemplate.findAllAccidentTypes();
    }

    public List<Rule> getAllRules() {
        return accidentJdbcTemplate.findAllRules();
    }
}
