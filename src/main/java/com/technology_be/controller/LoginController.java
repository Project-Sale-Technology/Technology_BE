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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/account")
public class LoginController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserRegisterDTO> handleRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        User user = new User();

        /* Get province by id */
        System.out.println(userRegisterDTO.getProvince());

        /* Set value for user */
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setProvince(province);
        user.setName(userRegisterDTO.getFullName());
        user.setCreateAt(LocalDate.now());

        /* Set authorization for user */
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_MEMBER");
        roles.add(role);
        user.setRoles(roles);

        /* Save user */
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Get list province */
    @RequestMapping(value = "/provinces" , method = RequestMethod.GET)
    public ResponseEntity<List<Province>> getProvinces() {
        List<Province> provinces = provinceService.findAll();
        if(provinces.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(provinces , HttpStatus.OK);
     }
}
