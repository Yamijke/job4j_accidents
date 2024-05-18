CREATE TABLE accidents (
  id serial primary key,
  name text,
  text text,
  address text,
  type_id int references accident_types(id)
);