package upc.backend.controller.literature.Param;
import lombok.Data;

@Data
public class LiteratureAddParam {
    private String LiterName;
    private String LiterType;
    private String LiterDigest;
    private String LiterAuthor;
    private String Sources;
    private String KeyWord;
    private String Url;
    private Integer TeamId;
    private Integer DownloadTimes;



}
