package pl.ug.project.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ug.project.domain.Role;

@Repository
public interface RoleRepo extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
