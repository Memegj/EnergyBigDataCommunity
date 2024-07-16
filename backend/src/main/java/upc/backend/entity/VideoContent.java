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
public class VideoContent {
    private Integer VideocontentId;
    private String VideocontentName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date UploadTime;

    private String VideocontentUrl;

    private Integer VidoeId;


}
