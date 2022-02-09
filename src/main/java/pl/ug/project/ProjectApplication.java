package pl.ug.project;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import pl.ug.project.config.importBeans;
import pl.ug.project.domain.User;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.Tag;

import pl.ug.project.repo.CommentRepo;
import pl.ug.project.repo.PostRepo;
import pl.ug.project.repo.TagRepo;
import pl.ug.project.services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
//@ImportResource({"classpath:beansPosts.xml", "classpath:beansComments.xml"})
public class ProjectApplication {
	@Autowired
	UserService userService;
	public static void main(String[] args){
		SpringApplication.run(ProjectApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner adminBean(UserService userService){
//		return (args -> {
//			User user = new User("admin", "admin", "admin2115", "admin@wp.pl", "Admin2115");
//			userService.registerNewUser(user, "ROLE_ADMIN");
//		});
//	}


//	@Bean
//	public CommandLineRunner csvToData(PostRepo postRepo){
//		String commentsFile = "./src/main/resources/static/Comments.csv";
//		String postsFile = "./src/main/resources/static/ManyPostsManyAuthors.csv";
//		importBeans importbeans = new importBeans();
//		JSONParser parser = new JSONParser();
////		importbeans.writeDataFromCsvToXml(commentsFile, "com");
////		importbeans.writeDataFromCsvToXml(postsFile, "post");
////		System.out.println("Import from CSV TO XML completed!");
//		List<String[]> commentsCSVdata = importbeans.getData(commentsFile);
//		List<String[]> postCSVdata = importbeans.getData(postsFile);
//		// na poczatku tagi, autorzy, potem posty i na koncu komentarze
//		return (args) -> {
//			for(String[] row: commentsCSVdata){
//				commentRepo.save(new Comment(Integer. parseInt(row[0]), row[1], new Post(), row[3]));
//			}
//			for(String[] row : postCSVdata){
//				String[] authors = row[1].substring(1, row[1].length() - 1).split(",");
//				ArrayList<Author> resultAuthors = new ArrayList<>();
//				List<Tag> tags = Arrays.stream(row[3].split(" ")).map(tag -> new Tag(tag)).collect(Collectors.toList());
//				for(String authorStr : authors){
//					JSONObject json = (JSONObject) parser.parse(authorStr);
//					resultAuthors.add(new Author(json.get("authors").toString()));
//				}
//				postRepo.save(new Post(Integer.parseInt(row[0]), resultAuthors, row[2], new ArrayList<Tag>(tags)));
//			}
//		};
}

