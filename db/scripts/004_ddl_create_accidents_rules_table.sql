CREATE TABLE accidents_rules (
  accident_id int references accidents(id),
  rule_id int references rules(id),
  PRIMARY KEY (accident_id, rule_id)
);