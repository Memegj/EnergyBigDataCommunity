package upc.backend.controller.video.param;
import lombok.Data;

@Data
public class VideoAddParam {
    private String VideoName;
    private String VideoTeacher;
    private String VideoIntro;
    private Integer UploadTime;
    private Integer PageView;
    private String Url;
    private Integer TeamId;
    private String Picture;

}
