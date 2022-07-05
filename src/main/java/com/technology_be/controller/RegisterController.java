package com.technology_be.controller;

import com.technology_be.dto.UserRegisterDTO;
import com.technology_be.model.Province;
import com.technology_be.model.Role;
import com.technology_be.model.User;
import com.technology_be.repository.RoleRepository;
import com.technology_be.service.ProvinceService;
import com.technology_be.service.UserService;
import com.technology_be.ultil.EncrypPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class RegisterController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    /* Register */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserRegisterDTO> handleRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO
            , BindingResult bindingResult) {
        User user = new User();
        User userCreated;

        /* Check error registration form */
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            /* Check user existing */
            if (userService.getUserByEmail(userRegisterDTO.getEmail()) != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                /* Get province by id */
                Province province = provinceService.getProvinceById(userRegisterDTO.getProvince());

                /* Set value for user */
                user.setEmail(userRegisterDTO.getEmail());
                user.setPassword(EncrypPasswordUtils.EncrypPasswordUtils(userRegisterDTO.getPassword()));
                user.setProvince(province);
                user.setName(userRegisterDTO.getFullName());
                user.setCreateAt(LocalDate.now());

                /* Set authorization for user */
                Set<Role> roles = new HashSet<>();
                Role role = roleRepository.findByName("ROLE_MEMBER");
                roles.add(role);
                user.setRoles(roles);

                /* Save user */
                userCreated = userService.saveUser(user);

                /* Set id for user */
                userRegisterDTO.setId(userCreated.getId());
                return new ResponseEntity<>(userRegisterDTO , HttpStatus.OK);
            }
        }
    }

    /* Check user existing */
    @RequestMapping(value = "/check-user-existing/e={email}", method = RequestMethod.GET)
    public ResponseEntity<User> checkUserExisting(@PathVariable("email") String email) {
        /* Get user */
        User user = userService.getUserByEmail(email);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* Get list province */
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public ResponseEntity<List<Province>> getProvinces() {
        List<Province> provinces = provinceService.findAll();
        if (provinces.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }
}
