package ru.alishev.springcourse.controllers;

//import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.dao.BookDAO;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Book;
import ru.alishev.springcourse.models.Person;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.show(id);
        Person currentPerson = bookDAO.showpeople(id);

        model.addAttribute("book", book);
        model.addAttribute("person", currentPerson);
        model.addAttribute("people", personDAO.index());

        if(currentPerson == null)
        return "books/show";
        else
            return "books/showpeople";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") Book book) {
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute(bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/assign")
    public String assignBook(@RequestParam("personId") int personId, @PathVariable("id") int id) {
       Person person = new Person();
       person.setId(personId);
       bookDAO.assign(person, id);
        return "redirect:/people";
    }

    @PostMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        Person currentPerson = bookDAO.showpeople(id);
        int personId = currentPerson.getId();
        bookDAO.free(currentPerson, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

}
