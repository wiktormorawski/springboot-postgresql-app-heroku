package pl.ug.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ug.project.domain.*;
import pl.ug.project.repo.CommentRepo;
import pl.ug.project.repo.PostRepo;
import pl.ug.project.repo.TagRepo;
import pl.ug.project.repo.UserRepo;

import org.springframework.mail.javamail.JavaMailSender;
import pl.ug.project.requestDomains.CommentRequest;
import pl.ug.project.requestDomains.PostRequest;
import pl.ug.project.services.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
public class PostController {
    private final JavaMailSender emailSender;
    private final UserRepo userRepo;
    private final PostService postService;
    private final UserService userService;
    private final TagRepo tagRepo;
    private final CommentService commentService;
    private final CommentRepo commentRepo;


    public PostController(@Autowired JavaMailSender emailSender, @Autowired UserRepo userRepo, @Autowired PostService postService, @Autowired UserService userService, @Autowired TagRepo tagRepo,@Autowired CommentRepo commentRepo, @Autowired CommentService commentService){
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.postService = postService;
        this.userService = userService;
        this.tagRepo = tagRepo;
        this.commentService = commentService;
        this.commentRepo = commentRepo;
    }
    @GetMapping("/error")
    public String redirectFromError(Model model){
        return "redirect:/";
    }
    @GetMapping("/deletePost/{id}")
    public String deletePost(Model model, @PathVariable String id, RedirectAttributes redirectAttributes){
        postService.deletePostById(Integer.parseInt(id));
        redirectAttributes.addFlashAttribute("message", "Delete of that Post succeeded :)");
        return "redirect:/";
    }
    @GetMapping("/deleteComment/{id}/{idPost}")
    public String deleteComment(Model model, @PathVariable String id,@PathVariable String idPost, RedirectAttributes redirectAttributes){
        Post post = postService.loadPostById(Integer.parseInt(idPost));
        post.deleteComment(Integer.parseInt(id));
        postService.addPost(post);
        redirectAttributes.addFlashAttribute("message", "Delete of that Comment succeeded :)");
        return "redirect:/";
    }
    @GetMapping("/editPost/{id}")
    public String editPost(Model model, @PathVariable String id, PostRequest postRequest){
        Post post = postService.loadPostById(Integer.parseInt(id));
        postRequest.setId(post.getId());
        postRequest.setContent(post.getContent());
        postRequest.setAuthors(new ArrayList<>(post.getAuthors()));
        postRequest.setTags(new ArrayList<>(post.getTagsNames()));
        model.addAttribute("search", new Search());
        model.addAttribute("postRequest", postRequest);
        model.addAttribute("authorsAvailable", userRepo.findAll());
        model.addAttribute("tagsAvailable", tagRepo.findAll());
        return "editPost";
    }
    @PostMapping("/editPost/{id}")
    public String editPostConfirm(Model model,RedirectAttributes redirectAttributes, @PathVariable String id,@Valid PostRequest postRequest,Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("message", "Edition of that Post failed, try again :(");
            model.addAttribute("search", new Search());
            model.addAttribute("authorsAvailable", userRepo.findAll());
            model.addAttribute("postRequest", postRequest);
            return "editPost";
        }

        ArrayList<Tag> tagsToAdd = createTags(postRequest);
        Post postEdited = new Post(postRequest.getAuthors(), postRequest.getContent(), tagsToAdd);
        Post editResult = postService.editPost(Integer.parseInt(id), postEdited);
        if(editResult == null){
            redirectAttributes.addFlashAttribute("postRequest", postRequest);
            redirectAttributes.addFlashAttribute("message", "Edition of that Post failed, try again :(");
        }
        redirectAttributes.addFlashAttribute("message", "Edition of that Post ended with success :)");
        return "redirect:/";
    }
    @GetMapping("/editComment/{id}")
    public String editComment(Model model, @PathVariable String id, CommentRequest commentRequest, Authentication auth){
        Comment comment = commentService.getCommentOfId(Integer.parseInt(id));
        commentRequest.setComment_content(comment.getComment_content());
        commentRequest.setId(comment.getId());
        commentRequest.setUsername(comment.getUsername());
        if(auth != null) {
            String userName = auth.getName();
            User user = userRepo.findByUserName(userName);
            if(user != null) {
               commentRequest.setUsername(user.getPublicUsername());
            }
        }
        model.addAttribute("search", new Search());
        model.addAttribute("commentRequest", commentRequest);
        return "editComment";
    }
    @PostMapping("/editComment/{id}")
    public String editCommentConfirm(Model model, @PathVariable String id, @Valid CommentRequest commentRequest, Errors errors, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute("message", "Edition of that Comment failed, resubmit with repaired values of that form :(");
            model.addAttribute("search", new Search());
            model.addAttribute("commentRequest", commentRequest);
            return "editComment";
        }
        Comment commentEdited = new Comment(commentRequest.getId(),commentRequest.getUsername(), commentRequest.getComment_content());
        Comment editResult = commentService.editComment(commentEdited);
        if(editResult == null){
            redirectAttributes.addFlashAttribute("commentRequest", commentRequest);
            redirectAttributes.addFlashAttribute("message", "Edition of that Comment failed, try again :(");
        }
        redirectAttributes.addFlashAttribute("message", "Edition of that Comment ended with success :)");
        return "redirect:/";
    }
    @PostMapping("/addComment/{i}")
    public String addCommentConfirm(Model model, @PathVariable String i, CommentRequest commentRequest, RedirectAttributes redirectAttributes, Authentication auth){
        Post post = postService.loadPostById(Integer.parseInt(i));
        if(auth != null){
            String userName = auth.getName();
            User user = userRepo.findByUserName(userName);
            Comment commentToAdd = new Comment(user.getPublicUsername(),commentRequest.getComment_content());
            Comment addResult = commentService.addComment(commentToAdd);
            if(addResult != null){
                redirectAttributes.addFlashAttribute("message", "Adding of that Comment ended with success :)");
                post.addComment(addResult);
                postService.addPost(post);
                return "redirect:/";
            }

            redirectAttributes.addFlashAttribute("message", "Adding of that Comment failed, try again :(");
        }
        else{
            Comment commentToAdd = new Comment(commentRequest.getUsername(), commentRequest.getComment_content());
            Comment addResult = commentService.addComment(commentToAdd);
            if(addResult == null){
                redirectAttributes.addFlashAttribute("message", "Adding of that Comment failed, try again :(");
                return "redirect:/";
            }
            post.addComment(addResult);
            postService.addPost(post);
            redirectAttributes.addFlashAttribute("message", "Adding of that Comment ended with success :)");
        }
        return "redirect:/";
    }
    @GetMapping("/addPost")
    public String addPost(Model model, PostRequest postRequest){
        model.addAttribute("search", new Search());
        model.addAttribute("postRequest", postRequest);
        model.addAttribute("authorsAvailable", userRepo.findAll());
        model.addAttribute("tagsAvailable", tagRepo.findAll());
        return "addPost";
    }
    public ArrayList<Tag> createTags(PostRequest post){
        ArrayList<Tag> tagsToAdd = new ArrayList<>();
        for(String tagName : post.getTags()){
            Tag foundTag = tagRepo.findByName(tagName);
            if(foundTag != null){
                tagsToAdd.add(foundTag);
            }else {
                Tag newTag = new Tag(tagName);
                Tag savedTag = tagRepo.save(newTag);
                tagsToAdd.add(savedTag);
            }
        }
        return tagsToAdd;
    }
    @PostMapping("/addPost")
    public String addPostConfirm(Model model,RedirectAttributes redirectAttributes, @Valid PostRequest postRequest, Errors errors ){
        if(errors.hasErrors()){
            model.addAttribute("message", "Adding of that Post failed, try again :(");
            model.addAttribute("search", new Search());
            model.addAttribute("authorsAvailable", userRepo.findAll());
            model.addAttribute("postRequest", postRequest);
            return "addPost";
        }
        ArrayList<Tag> tagsToAdd = createTags(postRequest);
        Post postToAdd = new Post(postRequest.getAuthors(), postRequest.getContent(), tagsToAdd);
        Post addResult = postService.addPost(postToAdd);
        if(addResult == null){
            redirectAttributes.addFlashAttribute("message", "Adding of that Post failed, try again :(");
            redirectAttributes.addFlashAttribute("postRequest", postRequest);
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("message", "Adding of that Post ended with success :)");
        return "redirect:/";
    }
    @GetMapping("/searchCommentsByAuthors")
    public String getCommentsByAuthors(Model model, Search searchComments){
        model.addAttribute("searchComments", searchComments);
        model.addAttribute("comments", new ArrayList<Comment>());
        if(searchComments.getContent() != null){
            List<Comment> comments = commentService.getCommentsOfName(searchComments.getContent());
            model.addAttribute("message", "Found "+comments.size()+" comment/comments written by "+searchComments.getContent());
            model.addAttribute("comments", comments);
        }
        return "searchCommentsByAuthors";
    }
    public double getAmountOfTags(List<Post> l){
        double sum = 0;
        double c = 0;
        for (Post p : l) {
            sum += p.getTagsSize();
            c += 1;
        }
        if(sum == 0 || c == 0){
            return 0;
        }
        return sum/c;
    }
    @GetMapping("/statistics")
    public String getStatistics(Model model, Search searchStatistics){
        model.addAttribute("searchStatistics", searchStatistics);
        model.addAttribute("statistics", new LinkedHashMap<>());
        if(searchStatistics.getContent() != null){
            Map<String, String> statistics = new HashMap<>();
            statistics.put("Amount of Comments", String.valueOf(commentService.getCommentsOfName(searchStatistics.getContent()).size()));
            statistics.put("Percentage of All comments", 0 + "%");
            if(commentRepo.findAll().size() > 0){
                statistics.put("Percentage of All comments", (commentService.getCommentsOfName(searchStatistics.getContent()).size())*100 / commentRepo.findAll().size() + "%");
            }
            statistics.put("Amount of Posts", String.valueOf(postService.getPostsOfName(searchStatistics.getContent()).size()));
            statistics.put("Percentage of All posts", 0 + "%");
            if(postService.getAllPosts().size() > 0){
                statistics.put("Percentage of All posts", (postService.getPostsOfName(searchStatistics.getContent()).size())*100 / postService.getAllPosts().size() + "%");
            }
            statistics.put("Average amount of tags added", String.valueOf(getAmountOfTags(postService.getPostsOfName(searchStatistics.getContent()))));
            model.addAttribute("message", "Loaded Statistics for "+searchStatistics.getContent());
            model.addAttribute("statistics", statistics);
        }
        return "statistics";
    }
    @GetMapping("/")
    public String home(Model model, Search search, Authentication auth) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("search", search);
        model.addAttribute("commentToAdd", new CommentRequest());
        SearchService searchService = new SearchService();
        SortService sortService = new SortService();
        List<Post> postsWithCommentsFiltered = new ArrayList<>();
        List<Post> postsWithCommentsFilteredSorted;
        if(search.getContent() == null || search.getContent().equals("") || search.getType() == null){
            postsWithCommentsFiltered = posts;
        }
        if(search.getContent() != null && !search.getContent().equals("") && search.getType() != null){
            postsWithCommentsFiltered = searchService.searchPosts(search, posts);
        }
        if(search.getSortContentType() == null || search.getSortContentType().equals("") || search.getSortOption() == null ){
            model.addAttribute("postsWithComments",postsWithCommentsFiltered);
        }
        if(search.getSortContentType() != null && !search.getSortContentType().equals("") && search.getSortOption() != null ){
            postsWithCommentsFilteredSorted = sortService.sortPosts(search, postsWithCommentsFiltered);
            model.addAttribute("postsWithComments",postsWithCommentsFilteredSorted);
        }
        else{
            model.addAttribute("postsWithComments",posts);
        }
        if(auth != null) {
            String userName = auth.getName();
            User user = userRepo.findByUserName(userName);
            if(user != null) {
                model.addAttribute("user", user);
            }
        }
        return "home";
    }
}
