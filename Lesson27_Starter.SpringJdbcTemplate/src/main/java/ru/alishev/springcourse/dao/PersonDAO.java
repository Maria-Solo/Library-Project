package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;

import java.util.List;

/**
 * @author Neil Alishev
 */
@Component
public class PersonDAO {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT person_id AS id, name, birth_year FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT person_id AS id, name, birth_year FROM person WHERE person_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
//        jdbcTemplate.update("INSERT INTO person VALUES(1, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail());
        jdbcTemplate.update("INSERT INTO person (name, birth_year) values(?, ?)", person.getName(), person.getBirth_year());
    }

    public void update(int id, Person updatedPerson) {
       jdbcTemplate.update("UPDATE person SET name=?, birth_year=? WHERE person_id=?", updatedPerson.getName(), updatedPerson.getBirth_year(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
    }




}
