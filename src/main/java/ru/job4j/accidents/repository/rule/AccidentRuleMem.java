package ru.job4j.accidents.repository.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentRuleMem implements AccidentRuleMemInterface {
    private AtomicInteger nextId = new AtomicInteger(1);
    private Map<Integer, AccidentRule> rules = new ConcurrentHashMap<>();

    @Autowired
    public AccidentRuleMem() {
        save(new AccidentRule(0, "Article 1"));
        save(new AccidentRule(0, "Article 2"));
        save(new AccidentRule(0, "Article 3"));
    }

    @Override
    public AccidentRule save(AccidentRule rule) {
        rule.setId(nextId.getAndIncrement());
        rules.put(rule.getId(), rule);
        return rule;
    }

    @Override
    public Optional<AccidentRule> findById(int id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public Collection<AccidentRule> findAll() {
        return rules.values();
    }

    @Override
    public boolean update(AccidentRule rule) {
        return rules.computeIfPresent(rule.getId(),
                (id, oldAccidentRule) -> new AccidentRule(oldAccidentRule.getId(), rule.getName())) != null;
    }

    @Override
    public void deleteById(int id) {
        rules.remove(id);
    }

    @Override
    public Collection<AccidentRule> findByIds(Collection<Integer> ids) {
        Collection<AccidentRule> result = new ArrayList<>();
        for (Integer id : ids) {
            findById(id).ifPresent(result::add);
        }
        return result;
    }
}
