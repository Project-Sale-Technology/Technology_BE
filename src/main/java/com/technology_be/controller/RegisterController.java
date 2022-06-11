package com.technology_be.controller;

import com.technology_be.dto.UserRegisterDTO;
import com.technology_be.model.Province;
import com.technology_be.model.Role;
import com.technology_be.model.User;
import com.technology_be.repository.RoleRepository;
import com.technology_be.service.ProvinceService;
import com.technology_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    /* Handle Register */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<User> registerForUser(@RequestBody UserRegisterDTO customer) {
        /* Set and get variable */
        User user = new User();
        User userSaved;
        Province province = provinceService.getProvinceById(customer.getProvince());

        user.setName(customer.getFullName());
        user.setEmail(customer.getEmail());
        user.setPassword(customer.getPassword());
        user.setProvince(province);

        /* Authorization */
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_MEMBER");
        roles.add(role);
        user.setRoles(roles);

        /* Save user */
        userSaved = userService.saveUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.OK);
    }
}
