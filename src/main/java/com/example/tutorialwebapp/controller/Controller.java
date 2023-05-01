package com.example.tutorialwebapp.controller;

import com.example.tutorialwebapp.loginDB.User;
import com.example.tutorialwebapp.serviceForBoth.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, HttpSession session){

        boolean f = userService.checkEMail(user.getEmail());
        if(f){
            session.setAttribute("msgAlreadyExist","Email existiert schon.");
        }else{
            User createdUser = userService.createUser(user);
            if(createdUser!= null){
                session.setAttribute("msgSuccess","Registrierung erfolgreich.");
            }else{
                session.setAttribute("msgError","Irgendetwas ist schief gelaufen.");
            }
        }

        return "redirect:/register";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }
}
