package pl.ug.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.User;
import pl.ug.project.repo.CommentRepo;
import pl.ug.project.repo.PostRepo;
import pl.ug.project.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    PostRepo postRepo;
    public Post addPost(Post post){
        return postRepo.save(post);
    }
    public List<Post> getAllPosts(){
        return (List<Post>) postRepo.findAll();
    }
    public List<Post> getPostsOfName(String name){
        List<Post> posts = (List<Post>) postRepo.findAll();
        List<Post> result = new ArrayList<>();
        for(Post post: posts){
            List<User> res = post.getAuthors().stream().filter(author -> (author.getName().substring(0,1) + author.getSurname()).toLowerCase().equals(name.toLowerCase())).collect(Collectors.toList());
            if(res.size() > 0){
                result.add(post);
            }
        }
        return result;
    }
    public Post loadPostById(Integer id){
        Optional<Post> post = postRepo.findById(id);
        if(post.isPresent()){
            return post.get();
        }
        return null;
    }
    public void deletePostById(Integer id){
        Post post = loadPostById(id);
        List<Comment> comments = post.getComments();
        for(Comment comment : comments){
            commentRepo.delete(comment);
        }
        for(User user : post.getAuthors()){
            user.removePost(post);
            userRepo.save(user);
        }
        post.setAuthors(null);
        postRepo.delete(post);
    }
    public Post editPost(Integer id, Post post){
        deletePostById(id);
        return postRepo.save(post);
    }
}
