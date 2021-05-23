package com.madwe.jodi.controllers;

import com.madwe.jodi.configs.JwtTokenProvider;
import com.madwe.jodi.domain.AuthBody;
import com.madwe.jodi.domain.User;
import com.madwe.jodi.repositories.UserRepository;
import com.madwe.jodi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/madwe")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody data, HttpServletRequest request) {
        try {

            LOGGER.info("Received a login request");
            String username = data.getEmailOrPhone();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findByEmailOrPhone(username, username), request);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return new ResponseEntity(model, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        LOGGER.info("Received a signup request");
        try {
            Optional<User> userExists = Optional.ofNullable(userService.findUserByEmail(user.getEmail()));
            if (userExists.isPresent()) {
                throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
            } else {
                //TODO has to be handled
                user.setRole("USER");
                userService.saveUser(user);
                Map<Object, Object> model = new HashMap<>();
                model.put("message", "SUCCESS");
                return new ResponseEntity(model, HttpStatus.OK);
            }
        } catch(Exception ex){
            LOGGER.error("Exception occurred while checking if user exists: ", ex);
            throw new BadCredentialsException("Exception occurred while saving username: " + user.getEmail()+":: Exception: "+ ex);
        }


    }
}
