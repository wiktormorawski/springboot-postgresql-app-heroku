package pl.ug.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.User;
import pl.ug.project.domain.Role;
import pl.ug.project.repo.UserRepo;
import org.springframework.security.core.userdetails.User.UserBuilder;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService implements UserManager {
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public RoleService roleService;
    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    public User registerNewUser(User user, String role) {
        Role userRole = roleService.getRole(role);
        user.setId(null);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.addRole(userRole);
        return userRepo.save(user);
    }

    public User registerNewUser(User user, String role, boolean encrypted) {
        Role userRole = roleService.getRole(role);
        user.setId(null);
        if(!encrypted) {
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }
        user.addRole(userRole);
        return userRepo.save(user);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserBuilder builder = null;
        List<User> listOfUsers = userRepo.findAll();
        User user = userRepo.findByUserName(userName);
        UserDetails userDetails = null;

        if (user == null) {
            log.info("User not found" + userName);
//            throw new IllegalArgumentException(login);
        } else {
            builder = org.springframework.security.core.userdetails.User.withUsername(userName);
            builder.password(user.getUserPassword());
            builder.authorities(mapRolesToAuthorities(user.getRoles()));
            userDetails = builder.build();
            log.info("Found user" + userDetails);
        }

        if (userDetails == null){
            throw new UsernameNotFoundException("Bad credentials");
        }
        return userDetails;
    }
}