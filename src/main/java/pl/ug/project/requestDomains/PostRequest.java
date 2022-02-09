package pl.ug.project.requestDomains;

import lombok.Data;
import org.springframework.lang.Nullable;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Tag;
import pl.ug.project.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
@Data
public class PostRequest {
    private Integer id;
    @NotNull(message = "Authors must be provided")
    @Size(min = 1, message = "Post must contain author/authors")
    private ArrayList<User> authors = new ArrayList<>();
    @Size(min = 3, message = "Post content can't be less than 3 characters")
    @NotEmpty(message = "Content of The Post can't be empty")
    private String content;

    @Nullable
    private ArrayList<String> tags = new ArrayList<>();

    public PostRequest(){}
}
