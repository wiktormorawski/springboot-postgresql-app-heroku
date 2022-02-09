package pl.ug.project.repo;

import org.springframework.stereotype.Repository;
import pl.ug.project.domain.Post;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface PostRepo extends CrudRepository<Post, Integer>{
}
