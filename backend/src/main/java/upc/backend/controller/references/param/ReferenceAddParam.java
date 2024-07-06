package upc.backend.controller.references.param;
import lombok.Data;

@Data
public class ReferenceAddParam {
    private String title;
    private String author;
    private Integer publish_year;
    private String file_path;
}
