package pl.ug.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.ug.project.domain.Tag;
import pl.ug.project.repo.TagRepo;

@Service
public class TagService {
    @Autowired
    @Lazy
    TagRepo tagRepo;
    public Tag addTag(Tag tag){
        return tagRepo.save(tag);
    }
}
