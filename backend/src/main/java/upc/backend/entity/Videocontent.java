package upc.backend.entity;

import java.util.Date;
import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Videocontent {
    private int VideocontentId;
    private String VideocontentName;
    private String VideocontentUrl;
    private int VideoId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date UploadTime;
    // Getter and Setter methods
    public Integer getVideocontentId() {
        return VideocontentId;
    }

    public void setVideocontentId(Integer VideocontentId) {
        this.VideocontentId = VideocontentId;
    }

    public String getVideocontentName() {
        return VideocontentName;
    }

    public void setVideocontentName(String VideocontentName) {
        this.VideocontentName = VideocontentName;
    }

    public String getVideocontentUrl() {
        return VideocontentUrl;
    }

    public void setVideocontentUrl(String VideocontentUrl) {
        this.VideocontentUrl = VideocontentUrl;
    }

    public Integer getVideoId() {
        return VideoId;
    }

    public void setVideoId(Integer VideoId) {
        this.VideoId = VideoId;
    }

    public Date getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(Date UploadTime) {
        this.UploadTime = UploadTime;
    }
}
