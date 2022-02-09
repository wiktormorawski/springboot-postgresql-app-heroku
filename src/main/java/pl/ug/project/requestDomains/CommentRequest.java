package pl.ug.project.requestDomains;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ug.project.domain.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@NoArgsConstructor
public class CommentRequest {
    private Integer id;
    @NotNull(message = "Username must be provided")
    @Size(min = 3, message = "Username can't be less than 3 characters")
    private String username;
    @NotNull(message = "Comment can't be empty")
    @Size(min = 3, message = "Comment content can't be less than 3 characters")
    private String comment_content;
    public CommentRequest(String username, String comment_content){
        this.username = username.toUpperCase();
        this.comment_content = comment_content;
    }
}