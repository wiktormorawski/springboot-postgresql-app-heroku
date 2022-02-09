package pl.ug.project.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.ug.project.domain.User;

public interface UserManager extends UserDetailsService {
    User save(User user);
}
