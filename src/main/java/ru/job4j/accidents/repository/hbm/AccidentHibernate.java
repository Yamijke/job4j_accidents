package ru.job4j.accidents.repository.hbm;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Repository
@AllArgsConstructor
public class AccidentHibernate {
    private final CrudRepository crudRepository;

    public Accident save(Accident accident) {
        crudRepository.run(session -> session.persist(accident));
        return accident;
    }

    public boolean update(Accident accident) {
        try {
            crudRepository.run(session -> session.merge(accident));
            return true;
        } catch (Exception e) {
            LOGGER.error("Cant update the accident" + e);
        }
        return false;
    }

    public List<Accident> findAll() {
        return crudRepository.query("select distinct t FROM Accident t "
                + "JOIN FETCH t.type p "
                + "left JOIN FETCH t.rules c "
                + "order by t.id ASC", Accident.class);
    }

    public Optional<Accident> findById(int id) {
        return crudRepository.optional(
                "from Accident where id = :fAccidentId", Accident.class,
                Map.of("fAccidentId", id)
        );
    }

    public void deleteById(int id) {
        crudRepository.run(
                "delete Accident where id = :fId",
                Map.of("fId", id)
        );
    }
}
