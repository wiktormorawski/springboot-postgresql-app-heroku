package pl.ug.project;

import pl.ug.project.config.importBeans;

public class FromCsvToBeans {
    public static void main(){
        String commentsFile = "./src/main/resources/static/Comments.csv";
        String postsFile = "./src/main/resources/static/ManyPostsManyAuthors.csv";
        importBeans importbeans = new importBeans();
        importbeans.writeDataFromCsvToXml(commentsFile, "com");
        importbeans.writeDataFromCsvToXml(postsFile, "post");
        System.out.println("Import from CSV TO XML completed!");
    }
}
