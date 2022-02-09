package pl.ug.project.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ug.project.domain.Role;
import pl.ug.project.repo.RoleRepo;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepo roleRepo;

    public Role getRole(String name) {
        Role role = roleRepo.findByName(name);
        if(role != null){
            return role;
        } else {
            return roleRepo.save(new Role(name));
        }
    }
}
