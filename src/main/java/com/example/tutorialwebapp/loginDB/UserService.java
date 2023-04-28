package com.example.tutorialwebapp.loginDB;

public interface UserService {

    public User createUser(User user);

    public boolean checkEMail(String mail);
}
