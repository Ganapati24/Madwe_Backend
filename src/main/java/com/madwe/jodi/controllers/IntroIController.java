package com.madwe.jodi.controllers;

import com.madwe.jodi.domain.IntroBody;
import com.madwe.jodi.repositories.IntroInterestsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/madwe")
public class IntroIController {

    private static Logger LOGGER = LoggerFactory.getLogger(IntroIController.class);

    @Autowired
    private IntroInterestsRepository introInterestsRepo;

    @PostMapping("/introi")
    public void saveIntroInterests(IntroBody introInterests){
        LOGGER.info("Received new interests for user: ", introInterests.getUserId());
        try{
            introInterestsRepo.save(introInterests);
        } catch (Exception ex){
            LOGGER.error("Exception occurred: ", ex);
        }
    }
}
