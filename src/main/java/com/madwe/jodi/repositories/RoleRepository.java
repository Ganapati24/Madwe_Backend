package com.madwe.jodi.repositories;

import com.madwe.jodi.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
