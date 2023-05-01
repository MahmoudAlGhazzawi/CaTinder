package com.example.tutorialwebapp.oAuth2;

import com.example.tutorialwebapp.loginDB.User;
import com.example.tutorialwebapp.repository.UserRepository;
import com.example.tutorialwebapp.serviceForBoth.UserService;
import com.example.tutorialwebapp.serviceForBoth.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        CustomerOAuth2User customerOAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String clientName = customerOAuth2User.getClientName();
        String email = customerOAuth2User.getEmail();
        String name = customerOAuth2User.getFullName();

        User toAddUser = new User();

        toAddUser.setClient(clientName);
        toAddUser.setRole("ROLE_USER");
        toAddUser.setEmail(email);
        toAddUser.setName(name);

        boolean f = userService.checkEMail(email);
        if(f){
                //"Email existiert schon."
        }else{
            User createdUser = userService.createUserOAuth(toAddUser);
            if(createdUser!= null){
                //"Registrierung erfolgreich."
            }else{
                //"Irgendetwas ist schief gelaufen."
            }
        }

        response.sendRedirect("/hello");
    }
}
