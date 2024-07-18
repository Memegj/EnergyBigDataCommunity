package upc.backend.controller.news.Param;
import cn.hutool.core.date.DateTime;
import lombok.Data;

@Data
public class NewsAddParam {
    private String title;
    private String ReleaseAuthor;
    private DateTime ReleaseTime;
    private String Url;
    private int isPublic;
    private String Content;
}
