package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;

import java.util.List;

@Component
public class BookDAO {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index () {
        return jdbcTemplate.query("SELECT book_id AS id, name, author, year FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show (int id) {
            return jdbcTemplate.query( "SELECT book_id AS id, name, author, year FROM book WHERE book_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                    .stream().findAny().orElse(null);
        }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book (name, author, year) values ( ?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void assign(Person person, int id) {
        jdbcTemplate.update("UPDATE book SET person_id = ? WHERE book_id=?", person.getId(), id);
    }

    public void free(Person person, int id) {
        jdbcTemplate.update("UPDATE book SET person_id=null WHERE book_id=? AND person_id=?", id, person.getId());
    }

    public List<Book> showall(int id){
       return jdbcTemplate.query("SELECT book_id AS id, book.name AS name, author, year FROM book join person on book.person_id = person.person_id WHERE person.person_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE book_id=?", updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", id);
    }

    public Person showpeople(int bookId) {
        String sql = "SELECT p.person_id AS id, p.name FROM person p JOIN book b ON p.person_id = b.person_id WHERE b.book_id = ?";
        return jdbcTemplate.query(sql, new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }
}
