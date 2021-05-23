package com.madwe.jodi.services;

import com.madwe.jodi.domain.Role;
import com.madwe.jodi.domain.User;
import com.madwe.jodi.repositories.RoleRepository;
import com.madwe.jodi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private PasswordEncoder bCryptPasswordEncoder;

        public User findUserByEmail(String email) {
            return userRepository.findByEmail(email);
        }

        public void saveUser(User user) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            //user.setEnabled(true);
            //Role userRole = roleRepository.findByRole("ADMIN");
            //user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            userRepository.save(user);
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            User user = userRepository.findByEmail(email);
            if(user != null) {
                List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
                return buildUserForAuthentication(user, authorities);
            } else {
                throw new UsernameNotFoundException("username not found");
            }
        }

        private List<GrantedAuthority> getUserAuthority(String role) {
            Set<GrantedAuthority> roles = new HashSet<>();
            roles.add(new SimpleGrantedAuthority(role));

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
            return grantedAuthorities;
        }

        private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
}
