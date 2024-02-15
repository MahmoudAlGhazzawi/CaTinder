package com.example.tutorialwebapp.controller;

import com.example.tutorialwebapp.loginDB.User;
import com.example.tutorialwebapp.serviceForBoth.UserService;
import com.example.tutorialwebapp.storage.ImageService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    private final String BUCKET_NAME = "catinder_bucket";



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

    /* @GetMapping("/images")
    public List<String> handleFileShow() {
        return imageService.getImageUrls();
    } */


    @GetMapping("/profil")
    public String showProfil(Model model) {
        List<String> imageUrls = imageService.getImageUrls();
        model.addAttribute("imageUrls", imageUrls);
        return "profil";
    }




    //@RequestMapping(value = "/deleteImages", method = RequestMethod.DELETE)
    @ResponseBody
    @DeleteMapping("/deleteImages")
    public ResponseEntity<Void> deleteImages(@RequestBody List<String> imageUrls) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = storage.get(BUCKET_NAME);

        for (String imageUrl : imageUrls) {
            System.out.println(imageUrl);
            String objectName = imageUrl.replace("https://storage.googleapis.com/" + BUCKET_NAME + "/", "");
            Blob blob = bucket.get(objectName);
            if (blob != null) {
                blob.delete();
            }
        }

        return ResponseEntity.ok().build();
    }



    /*
    @RequestMapping(value = "/deleteImages", method = RequestMethod.DELETE)
    @ResponseBody
    //@DeleteMapping("/deleteImages")
    public String deleteImages(@RequestBody List<String> imageUrls){
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = storage.get(BUCKET_NAME);

        for (String imageUrl : imageUrls) {
            System.out.println(imageUrl);
            String objectName = imageUrl.replace("https://storage.googleapis.com/" + BUCKET_NAME + "/", "");
            Blob blob = bucket.get(objectName);
            if (blob != null) {
                blob.delete();
            }
        }

        return "profil";
    }

     */


}
