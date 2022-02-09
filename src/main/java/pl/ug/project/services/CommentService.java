package pl.ug.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.User;
import pl.ug.project.repo.CommentRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    @Lazy
    CommentRepo commentRepo;
    public Comment addComment(Comment comment){
        return commentRepo.save(comment);
    }
    public Comment getCommentOfId(Integer id){
        Optional<Comment> comment = commentRepo.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }
        return null;
    }
    public List<Comment> getCommentsOfName(String name){
        return commentRepo.findCommentsByUsername(name);
    }
    public Comment editComment(Comment comment){
        return commentRepo.save(comment);
    }
}
