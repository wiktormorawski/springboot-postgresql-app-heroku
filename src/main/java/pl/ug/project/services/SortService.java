package pl.ug.project.services;

import org.springframework.stereotype.Service;
import pl.ug.project.domain.Comment;
import pl.ug.project.domain.Post;
import pl.ug.project.domain.Search;

import java.util.*;
import java.util.Map.Entry;


@Service
public class SortService {

    public List<Post> sortPosts(Search search, List<Post> postsWithComments){
        List<Post> result = new ArrayList<>(postsWithComments);
        Collections.sort(result, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                if(search.getSortOption().equals("ASC")){
                    if(search.getSortContentType().equals("tag")){
                        return ((Comparable<Integer>) ((Post) (o1)).getTags().size()).compareTo(((Post) (o2)).getTags().size());
                    }
                    if(search.getSortContentType().equals("author")){
                        return ((Comparable<Integer>) ((Post) (o1)).getAuthors().size()).compareTo(((Post) (o2)).getAuthors().size());
                    }
                    if(search.getSortContentType().equals("content")){
                        return ((Comparable<Integer>) ((Post) (o1)).getContent().length()).compareTo(((Post) (o2)).getContent().length());
                    }
                }
                if(search.getSortOption().equals("DESC")){
                    if(search.getSortContentType().equals("tag")){
                        return ((Comparable<Integer>) ((Post) (o2)).getTags().size()).compareTo(((Post) (o1)).getTags().size());
                    }
                    if(search.getSortContentType().equals("author")){
                        return ((Comparable<Integer>) ((Post) (o2)).getAuthors().size()).compareTo(((Post) (o1)).getAuthors().size());
                    }
                    if(search.getSortContentType().equals("content")){
                        return ((Comparable<Integer>) ((Post) (o2)).getContent().length()).compareTo(((Post) (o1)).getContent().length());
                    }
                }
                return 1;
            }
        });

        return result;
    }

}
