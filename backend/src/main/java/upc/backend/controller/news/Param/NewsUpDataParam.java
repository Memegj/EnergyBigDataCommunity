package upc.backend.controller.news.Param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class NewsUpDataParam {
    private Integer tid;
    private String title;
    private String release;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String url;
    private String content;
}
