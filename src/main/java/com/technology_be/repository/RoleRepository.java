package com.technology_be.repository;

import com.technology_be.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role , Integer> {
    @Query("select r from Role r where r.name =?1")
    Role findByName(String name);
}
