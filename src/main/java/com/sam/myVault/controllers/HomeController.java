package com.sam.myVault.controllers;

//import com.sam.todoApp.repositories.TodoRepository;
//import com.sam.todos.services.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/welcome")
public class HomeController {
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/home")
    public String homePage() {
        return "Home";
    }
    @GetMapping("/services")
    public String servicePage() {
        return "Services";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "About";
    }
    @GetMapping("/contact")
    public String contactPage() {
        return "Contact";
    }
    @GetMapping("/userhomhe")
    public String userPage() {
        return "UserHome";
    }
    @GetMapping("/news")
    public String newstory() {
        return "New";
    }


}