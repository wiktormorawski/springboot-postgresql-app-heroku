package pl.ug.project.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Search {
    @NotNull(message = "Search field must be filled to filter :)")
    private String content;
    @NotNull(message="Choose type of search")
    private String type;
    @NotNull(message="Content type to sort must be provided")
    private String sortContentType;
    @NotNull(message="desc or asc must be provided")
    private String sortOption;
}
