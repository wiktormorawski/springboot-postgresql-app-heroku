package pl.ug.project.domain;

import lombok.Data;
import pl.ug.project.validators.ValidEmail;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Surname can't be empty")
    private String surname;

    @NotBlank(message = "Email can't be empty")
    @ValidEmail
    private String email;

    @NotBlank(message = "Login can't be empty")
    private String userName;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 7, message = "Password must be longer than 6 characters")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).+$", message="Uppercase letters, lowercase letters and digits are required")
    private String userPassword;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<Role>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    public User(){}

    public User(String name, String surname, String userName, String email, String userPassword) {
        this.userName = userName;
        this.email = email;
        this.userPassword = userPassword;
        this.name = name;
        this.surname = surname;
    }
    public void addRole(Role role){
        roles.add(role);
    }
    public void addPost(Post post){
        posts.add(post);
    }
    public boolean checkIfAdmin(){
        List<Role> adminRoles = roles.stream().filter(role -> Objects.equals(role.getName(), "ROLE_ADMIN")).collect(Collectors.toList());
        return adminRoles.size() > 0;
    }
    public String getPublicUsername(){
        return (getName().substring(0,1) + getSurname()).toUpperCase();
    }
    public void removePost(Post post){
        posts.remove(post);
    }
}
