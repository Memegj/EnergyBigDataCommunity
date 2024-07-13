package upc.backend.entity;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class video {
    private Integer VideoId;
    private String VideoName;
    private String VideoTeacher;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date UploadTime;
    private String VideoIntro;
    private Integer isPublic;
    private Integer PageView;//浏览量
    private String Url;
    private Integer UserId;



}
