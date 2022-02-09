package pl.ug.project.repo;

import org.springframework.stereotype.Repository;
import pl.ug.project.domain.Comment;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Integer>{
    @Override
    List<Comment> findAll();
    List<Comment> findCommentsByUsername(String name);
}