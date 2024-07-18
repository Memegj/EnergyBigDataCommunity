package upc.backend.controller.news.Param;

import lombok.Data;

import java.sql.Date;

@Data
public class NewsP {
    private Integer tid;
    private String title;
    private String release;
    private Date time;
    private String url;
    private String content;
}
