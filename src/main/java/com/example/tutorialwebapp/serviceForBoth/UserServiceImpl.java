package com.example.tutorialwebapp.serviceForBoth;

import com.example.tutorialwebapp.loginDB.User;
import com.example.tutorialwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setClient("Lokal");
        return userRepository.save(user);
    }

    @Override
    public User createUserOAuth(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean checkEMail(String mail) {
        return userRepository.existsByEmail(mail);
    }

    @Override
    public User getUserByMail(String mail) {
        return userRepository.findByEmail(mail);
    }
}
