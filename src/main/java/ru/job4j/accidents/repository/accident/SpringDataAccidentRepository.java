package ru.job4j.accidents.repository.accident;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Accident;

public interface SpringDataAccidentRepository extends CrudRepository<Accident, Integer> {
}
