package upc.backend.controller.literature.Param;

import lombok.Data;

@Data
public class LiteratureUpdateParam {
    private Integer LiterId;
    private String LiterName;
    private String LiterDigest;
    private String LiterType;
    private String LiterAuthor;
    private String Sources;
    private String KeyWord;
    private String Url;
    private Integer DownloadTimes;
    private Integer TeamId;
}
