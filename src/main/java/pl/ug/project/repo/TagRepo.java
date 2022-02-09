package pl.ug.project.repo;

import org.springframework.stereotype.Repository;
import pl.ug.project.domain.Tag;

import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Repository
public interface TagRepo extends CrudRepository<Tag, Integer>{
    @Override
    List<Tag> findAll();
    Tag findByName(@NotBlank String name);
}
