package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public Accident save(Accident accident) {
        jdbc.update("insert into accidents (name, text, address, type_id) values (?, ?, ?, ?)",
                accident.getName(), accident.getText(), accident.getAddress(), accident.getType().getId());
        int accidentId = jdbc.queryForObject("select currval(pg_get_serial_sequence('accidents','id'))", Integer.class);
        accident.setId(accidentId);
        if (accident.getRules() != null) {
            accident.getRules().forEach(rule -> jdbc.update("insert into accidents_rules (accident_id, rule_id) values (?, ?)",
                    accidentId, rule.getId()));
        }
        return accident;
    }

    public Optional<Accident> findById(int id) {
        Accident accident = jdbc.queryForObject("select a.id, a.name, a.text, a.address, t.id as type_id, t.name as type_name from accidents a "
                        + "join accident_types t on a.type_id = t.id where a.id = ?",
                (rs, row) -> {
                    Accident acc = new Accident();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setText(rs.getString("text"));
                    acc.setAddress(rs.getString("address"));
                    AccidentType type = new AccidentType(rs.getInt("type_id"), rs.getString("type_name"));
                    acc.setType(type);
                    return acc;
                }, id);
        List<Rule> rules = jdbc.query("select r.id, r.name from rules r "
                        + "join accidents_rules ar on r.id = ar.rule_id where ar.accident_id = ?",
                (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name")), id);
        assert accident != null;
        accident.setRules(new HashSet<>(rules));
        return Optional.of(accident);
    }

    public List<Accident> findAll() {
        List<Accident> accidents = jdbc.query("select a.id, a.name, a.text, a.address, t.id as type_id, t.name as type_name from accidents a "
                        + "join accident_types t on a.type_id = t.id",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    AccidentType type = new AccidentType(rs.getInt("type_id"), rs.getString("type_name"));
                    accident.setType(type);
                    return accident;
                });
        accidents.forEach(accident -> {
            List<Rule> rules = jdbc.query("select r.id, r.name from rules r "
                            + "join accidents_rules ar on r.id = ar.rule_id where ar.accident_id = ?",
                    (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name")), accident.getId());
            accident.setRules(new HashSet<>(rules));
        });
        return accidents;
    }

    public boolean update(Accident accident) {
        int updated = jdbc.update("update accidents set name = ?, text = ?, address = ?, type_id = ? where id = ?",
                accident.getName(), accident.getText(), accident.getAddress(), accident.getType().getId(), accident.getId());
        jdbc.update("delete from accidents_rules where accident_id = ?", accident.getId());
        accident.getRules().forEach(rule -> jdbc.update("insert into accidents_rules (accident_id, rule_id) values (?, ?)",
                accident.getId(), rule.getId()));
        return updated > 0;
    }

    public void deleteById(int id) {
        jdbc.update("delete from accidents_rules where accident_id = ?", id);
        jdbc.update("delete from accidents where id = ?", id);
    }

    public List<AccidentType> findAllAccidentTypes() {
        return jdbc.query("select id, name from accident_types",
                (rs, row) -> new AccidentType(rs.getInt("id"), rs.getString("name")));
    }

    public List<Rule> findAllRules() {
        return jdbc.query("select id, name from rules",
                (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name")));
    }
}