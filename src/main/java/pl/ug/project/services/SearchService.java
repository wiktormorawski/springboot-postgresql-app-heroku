package pl.ug.project.services;

import org.springframework.stereotype.Service;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    public List<Post> searchPosts(Search search, List<Post> postsWithComments){
        if(search.getType().equals("tag")){
            return searchByTag(search.getContent(), postsWithComments);
        }
        if(search.getType().equals("content")){
            return searchByContent(search.getContent(), postsWithComments);
        }
        if(search.getType().equals("author")){
            return searchByAuthor(search.getContent(), postsWithComments);
        }
        else{
            return postsWithComments;
        }
    }
    private List<Post> searchByTag(String content,List<Post> postsWithComments){
        List<Post> result = new ArrayList<>();
        for (Post post: postsWithComments){
            if(post.getTagsNames().contains(content.toUpperCase())){
                result.add(post);
            }
        }
        return result;
    }
    private List<Post> searchByContent(String content, List<Post> postsWithComments){
        List<Post> result = new ArrayList<>();
        for (Post post : postsWithComments){
            if(post.getContent().toUpperCase().contains(content.toUpperCase())){
                result.add(post);
            }
        }
        return result;
    }
    private List<Post> searchByAuthor(String content, List<Post> postsWithComments){
        List<Post> result = new ArrayList<>();
        for (Post post : postsWithComments){
            if(post.getAuthorsNames().contains(content.toUpperCase())){
                result.add(post);
            }
        }
        return result;
    }
}
