//package pl.ug.project.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import pl.ug.project.domain.Post;
//import pl.ug.project.domain.Search;
//import pl.ug.project.services.PostManager;
//import pl.ug.project.services.SearchService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//public class ApiPostController {
//    private final PostManager postManager;
//    private  final SearchService searchService;
//    public ApiPostController(@Autowired PostManager postManager, @Autowired SearchService searchService){
//        this.postManager = postManager;
//        this.searchService = searchService;
//    }
//    @ResponseBody
//    @RequestMapping("/api/getPost/{id}")
//    public Post getPostJson(@PathVariable String id){
//        return postManager.getPost(id);
//    }
//    @ResponseBody
//    @RequestMapping("/api/getPostByAuthor/{name}")
//    public List<Post> getPostByAuthorJson(@PathVariable String name){
//        return postManager.readPosts().stream().filter(post -> post.getAuthors().contains(name)).collect(Collectors.toList());
//    }
//}
