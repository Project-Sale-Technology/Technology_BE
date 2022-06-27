package com.technology_be.repository;

import com.technology_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query(value = "select * from `users` where `users`.email=?1 and `users`.password=?2", nativeQuery=true)
    User getUserByEmailAndPassword(String email , String password);

    /* Find user by reset token */
    @Query(value = "select * from `users` where resetPasswordToken=?1" , nativeQuery=true)
    User findUserByResetPasswordToken(String token);
}
