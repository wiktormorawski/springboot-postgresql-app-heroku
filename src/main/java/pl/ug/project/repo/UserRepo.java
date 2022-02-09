package pl.ug.project.repo;

import org.springframework.stereotype.Repository;
import pl.ug.project.domain.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Integer>{
    User findByUserName(String userName);
    @Override
    List<User> findAll();
}
