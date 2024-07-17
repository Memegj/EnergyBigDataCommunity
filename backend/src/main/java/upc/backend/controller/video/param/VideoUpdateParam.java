package upc.backend.controller.video.param;

import lombok.Data;

@Data
public class VideoUpdateParam {
    private Integer VideoId;
    private String VideoName;
    private String VideoTeacher;
    private Integer  UploadTime;
    private String Url;
}
