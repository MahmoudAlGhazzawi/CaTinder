package com.example.tutorialwebapp.serviceForBoth;

import com.example.tutorialwebapp.loginDB.User;

public interface UserService {

    public User createUser(User user);

    User createUserOAuth(User user);

    public boolean checkEMail(String mail);

    public User getUserByMail(String mail);
}
