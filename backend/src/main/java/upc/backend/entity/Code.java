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
    private Integer codeId;
    private String codeName;
    private Date uploadTime;
    private String codeAbstract;
    private String codeDetails;
    private Integer isPublic;
    private Integer downLoadTimes;
    private Integer userId;
    private String url;
    private String userName;
    private Integer teamId;
    private String teamName;
    // 添加这个字段来存储用户名
}

