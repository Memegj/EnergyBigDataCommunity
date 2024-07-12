package upc.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class Datasets {
    private Integer DataId;
    private String DataName;
    private Date UploadTime;
    private String DataAbstract;
    private String DataDetails;
    private Integer DownloadTimes;
    private Integer UserId;
    private Integer TeamId;
    private String Url;
    private String TeamName;
    private String UserName;
    private long FileSize;
    private Integer CollectId;


}
