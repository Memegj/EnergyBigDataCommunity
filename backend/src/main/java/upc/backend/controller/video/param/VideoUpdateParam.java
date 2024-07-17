package upc.backend.controller.video.param;

import lombok.Data;

@Data
public class VideoUpdateParam {
    private Integer VideoId;
    private String VideoName;
    private String VideoTeacher;
    private String VideoIntro;
    private String Picture;
    private Integer TeamId;
    private Integer PageView;
    private String Url;
}
