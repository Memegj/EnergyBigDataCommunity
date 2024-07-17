package upc.backend.controller.video.param;
import lombok.Data;

@Data
public class VideocontentAddParam {
    private String VideocontentName;
    private String UploadTime;
    private Integer VideoId;
    private String VideocontentUrl;
}
