package pl.ug.project.domain;
import org.json.*;
import lombok.Data;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.lang.Nullable;
import pl.ug.project.repo.TagRepo;
import pl.ug.project.services.TagService;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Authors must be provided")
    @Size(min = 1, message = "Post must contain author/authors")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<User> authors = new ArrayList<>();
    @Size(min = 3, message = "Post content can't be less than 3 characters")
    @NotEmpty(message = "Content of The Post can't be empty")
    @Column(name = "content", length = 1024)
    private String content;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @Nullable
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    public Post(ArrayList<User> authors, String content, ArrayList<Tag> tags) {
        System.out.println("CREATING POST: " + authors + content + tags);
        this.authors = authors;
        this.content = content;
        this.tags = tags;
    }
    public Post(Integer id, ArrayList<User> authors, String content, ArrayList<Tag> tags) {
        this.id = id;
        this.authors = authors;
        this.content = content;
        this.tags = tags;
    }
//    public ArrayList<String> convertAuthors(ArrayList<Author> authors){
//        return (ArrayList<String>) authors.stream().map(author -> {
//            try {
//                return new JSONObject(author).getString("authors").toUpperCase();
//            } catch (JSONException e) {
//                try {
//                    return new JSONObject(author.substring(1)).getString("authors").toUpperCase();
//                } catch (JSONException ex) {
//                    ex.printStackTrace();
//                }
//            }
//            return null;
//        }).collect(Collectors.toList());
//    }
    public int getTagsSize(){
        return getTags().size();
    }
//    public ArrayList<String> convertTags(String tags){
//        ArrayList<String> tagsList = new ArrayList( Arrays.asList(tags.split(" ")));
//        return (ArrayList<String>) tagsList.stream().map(tag -> tag.toUpperCase()).collect(Collectors.toList());
//    }
    public List<String> getTagsNames(){
        List<String> result = new ArrayList<>();
        for(Tag tag : this.tags){
            result.add(tag.getName().toUpperCase());
        }
        return result;
    }
    public List<String> getAuthorsNames(){
        List<String> result = new ArrayList<>();
        for(User user : authors){
            result.add((user.getName().substring(0,1) + user.getSurname()).toUpperCase());
        }
        return result;
    }
    public Post(){}
    public void addComment(Comment comment){
        this.comments.add(comment);
    }
    public void deleteComment(Integer commentId){
        Comment commentFound = null;
        for(Comment comment : comments){
            if(comment.getId().equals(commentId)){
                commentFound = comment;
                break;
            }
        }
        if(commentFound != null){
            this.comments.remove(commentFound);
        }
    }
}
