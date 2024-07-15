package upc.backend.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class Code {
    private Integer CodeId;
    private String CodeName;
    private Date UploadTime;
    private String CodeAbstract;
    private String CodeDetails;
    private Integer DownloadTimes;
    private Integer UserId;
    private String Url;
    private String UserName;
    private Integer TeamId;
    private String TeamName;
    private long FileSize;
    private Integer CollectId;
    // 添加这个字段来存储用户名
}

