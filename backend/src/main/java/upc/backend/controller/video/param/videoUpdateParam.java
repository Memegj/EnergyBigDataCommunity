package upc.backend.controller.video.param;

import lombok.Data;

@Data
public class videoUpdateParam {
    private Integer VideoId;
    private String VideoName;
    private String VideoTeacher;
    private Integer  UploadTimer;
    private String Url;
}
