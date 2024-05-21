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
        String sql = "SELECT a.id, a.name, a.text, a.address, a.type_id, t.name as type_name, "
                + "r.id as rule_id, r.name as rule_name "
                + "FROM accidents a "
                + "JOIN accident_types t ON a.type_id = t.id "
                + "LEFT JOIN accidents_rules ar ON a.id = ar.accident_id "
                + "LEFT JOIN rules r ON ar.rule_id = r.id";
        return jdbc.query(sql, rs -> {
            Map<Integer, Accident> accidentMap = new HashMap<>();
            while (rs.next()) {
                int accidentId = rs.getInt("id");
                Accident accident = accidentMap.get(accidentId);
                if (accident == null) {
                    accident = new Accident();
                    accident.setId(accidentId);
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type_name")));
                    accident.setRules(new HashSet<>());
                    accidentMap.put(accidentId, accident);
                }
                int ruleId = rs.getInt("rule_id");
                if (ruleId > 0) {
                    AccidentRule rule = new AccidentRule();
                    rule.setId(ruleId);
                    rule.setName(rs.getString("rule_name"));
                    accident.getRules().add(rule);
                }
            }
            return new ArrayList<>(accidentMap.values());
        });
    }

    public Optional<Accident> findById(int id) {
        String sql = "SELECT a.id, a.name, a.text, a.address, a.type_id, t.name as type_name, "
                + "r.id as rule_id, r.name as rule_name "
                + "FROM accidents a "
                + "JOIN accident_types t ON a.type_id = t.id "
                + "LEFT JOIN accidents_rules ar ON a.id = ar.accident_id "
                + "LEFT JOIN rules r ON ar.rule_id = r.id "
                + "WHERE a.id = ?";
        List<Accident> accidents = jdbc.query(sql, rs -> {
            Map<Integer, Accident> accidentMap = new HashMap<>();
            while (rs.next()) {
                int accidentId = rs.getInt("id");
                Accident accident = accidentMap.get(accidentId);
                if (accident == null) {
                    accident = new Accident();
                    accident.setId(accidentId);
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type_name")));
                    accident.setRules(new HashSet<>());
                    accidentMap.put(accidentId, accident);
                }
                int ruleId = rs.getInt("rule_id");
                if (ruleId > 0) {
                    AccidentRule rule = new AccidentRule();
                    rule.setId(ruleId);
                    rule.setName(rs.getString("rule_name"));
                    accident.getRules().add(rule);
                }
            }
            return new ArrayList<>(accidentMap.values());
        }, id);
        return accidents.isEmpty() ? Optional.empty() : Optional.of(accidents.get(0));
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
