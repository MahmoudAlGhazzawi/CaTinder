package com.example.tutorialwebapp.controller;

import com.example.tutorialwebapp.loginDB.User;
import com.example.tutorialwebapp.serviceForBoth.UserService;
import com.example.tutorialwebapp.storage.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;


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

        return "redirect:/welcome_page";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String imageUrl = imageService.uploadImage(file);
            }
        }
        return "profil";
    }



}
