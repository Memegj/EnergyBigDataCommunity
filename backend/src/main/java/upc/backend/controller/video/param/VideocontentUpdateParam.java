package upc.backend.controller.video.param;

import lombok.Data;

@Data
public class VideocontentUpdateParam {
    private Integer VideocontentId;
    private String VideocontentName;
    private String VideocontentUrl;
}
