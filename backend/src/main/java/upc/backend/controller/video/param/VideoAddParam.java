package upc.backend.controller.video.param;
import lombok.Data;

@Data
public class VideoAddParam {
    private String VideoName;
    private String VideoTeacher;
    private Integer UploadTime;
    private String Url;
}
