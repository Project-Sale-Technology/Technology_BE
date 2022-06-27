package com.technology_be.service;

import com.technology_be.model.Role;
import com.technology_be.model.User;
import com.technology_be.repository.UserRepository;
import com.technology_be.ultil.EncrypPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    /* Save User */
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /* Get user by email */
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    /* Get user by email and password */
    public User getUserByEmailAndPassword(String email , String password){
        return this.userRepository.getUserByEmailAndPassword(email , password);
    }

    /* set password token for user */
    public User setPasswordToken(String token , String email) {
        User user = userRepository.findByEmail(email);
        if(user!=null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    /* Get user by token password */
    public User getUserByPasswordToken(String token) {
        return this.userRepository.findUserByResetPasswordToken(token);
    }

    /* Update password */
    public User updatePassword(User user , String newPassword) {
        user.setPassword(EncrypPasswordUtils.EncrypPasswordUtils(newPassword));
        /* Remove token password */
        user.setResetPasswordToken(null);
        return userRepository.save(user);
    }

    /* Check user */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), grantedAuthorities
        );
    }
}