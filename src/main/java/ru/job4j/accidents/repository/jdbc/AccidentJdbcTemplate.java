package ru.job4j.accidents.repository.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentRule;
import ru.job4j.accidents.model.AccidentType;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public Accident save(Accident accident) {
        final String INSERT_SQL = "insert into accidents (name, text, address, type_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);
        accident.setId(keyHolder.getKey().intValue());
        for (AccidentRule rule : accident.getRules()) {
            jdbc.update("insert into accidents_rules (accident_id, rule_id) values (?, ?)",
                    accident.getId(),
                    rule.getId());
        }
        return accident;
    }

    public boolean update(Accident accident) {
        try {
            int id = accident.getId();
            int rowsAffected = jdbc.update("update accidents set name = ?, text = ?, address = ?, type_id = ?"
                            + " where id = ?",
                    accident.getName(),
                    accident.getText(),
                    accident.getAddress(),
                    accident.getType().getId(),
                    id);
            if (rowsAffected == 0) {
                return false;
            }
            jdbc.update("delete from accidents_rules where accident_id = ?", id);
            for (AccidentRule rule : accident.getRules()) {
                jdbc.update("insert into accidents_rules (accident_id, rule_id) "
                                + "values (?, ?)",
                        id,
                        rule.getId());
            }
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
        }
        return false;
    }

    public List<Accident> findAll() {
        return jdbc.query("select a.id, a.name, a.text, a.address, a.type_id, t.name as type_name "
                        + "from accidents a "
                        + "join accident_types t on a.type_id = t.id",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type_name")));
                    accident.setRules(Set.of(new AccidentRule()));
                    return accident;
                });
    }

    public Optional<Accident> findById(int id) {
        Accident res = jdbc.queryForObject("select id, name, text, address, type_id "
                        + "from accidents where id = ?",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), ""));
                    return accident;
                }, id);
        List<AccidentRule> rules = jdbc.query("select rule_id from accidents_rules where accident_id = ?",
                (rs, row) -> {
                    AccidentRule rule = new AccidentRule();
                    rule.setId(rs.getInt("rule_id"));
                    return rule;
                }, id);
        res.setRules(new HashSet<>(rules));
        return Optional.ofNullable(res);
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return jdbc.query("select id, name from accident_types",
                (rs, row) -> {
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("id"));
                    accidentType.setName(rs.getString("name"));
                    return accidentType;
                });
    }

    public void deleteById(int id) {
        jdbc.update("delete from accidents_rules where accident_id = ?", id);
        jdbc.update("delete from accidents where id = ?", id);
    }

    public Collection<AccidentRule> findRulesByIds(List<Integer> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbc.query(String.format("select r.id, r.name from rules r where r.id in (%s)", inSql),
                (rs, row) -> {
                    AccidentRule rule = new AccidentRule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, ids.toArray());
    }
}
