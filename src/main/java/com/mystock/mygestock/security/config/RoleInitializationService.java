package com.mystock.mygestock.security.config;

import com.mystock.mygestock.entity.Roles;
import com.mystock.mygestock.repository.RolesRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleInitializationService {

    @Autowired
    private RolesRepository roleRepository;

    public RoleInitializationService(RolesRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByRoleName("ROLE_USER").isEmpty()) {
            Roles userRole = new Roles();
            userRole.setRoleName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByRoleName("ROLE_ADMIN").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setRoleName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }
}
