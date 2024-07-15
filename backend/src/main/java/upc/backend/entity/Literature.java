package upc.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class Literature {
    private Integer LiterId;
    private String LiterName;
    private Date UploadTime;
    private String LiterType;
    private String LiterAuthor;
    private String LiterDigest;
    private String Sources;
    private String KeyWord;
    private Integer DownloadTimes;
    private Integer UserId;
    private Integer TeamId;
    private String Url;
    private String TeamName;
    private String UserName;
    private long FileSize;
    private Integer CollectId;


}
