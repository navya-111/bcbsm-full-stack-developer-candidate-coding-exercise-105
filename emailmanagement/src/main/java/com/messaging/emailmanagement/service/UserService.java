package com.messaging.emailmanagement.service;

import com.messaging.emailmanagement.config.EncryptionUtil;
import com.messaging.emailmanagement.data.entity.User;
import com.messaging.emailmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) throws Exception {
        user.setPassword(EncryptionUtil.encrypt(user.getPassword()));
        userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findById(userId).get();
    }


    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).get();

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority());
    }


    public List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}