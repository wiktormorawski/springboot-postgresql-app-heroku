package pl.ug.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Username must be provided")
    @Size(min = 3, message = "Username can't be less than 3 characters")
    private String username;
    @NotNull(message = "Comment can't be empty")
    @Size(min = 3, message = "Comment content can't be less than 3 characters")
    @Column(name = "comment_content", length = 1024)
    private String comment_content;
    public Comment(String username, String comment_content){
        this.username = username.toUpperCase();
        this.comment_content = comment_content;
    }
    public Comment(){}

    public Comment(Integer id, String username, String comment_content) {
        this.id = id;
        this.username = username;
        this.comment_content = comment_content;
    }
}
