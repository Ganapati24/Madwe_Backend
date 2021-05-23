package com.madwe.jodi.repositories;

import com.madwe.jodi.domain.IntroBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroInterestsRepository extends MongoRepository<IntroBody, String> {

}
