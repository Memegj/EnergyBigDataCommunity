package upc.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class Reference {
    private Integer id;
    private String title;
    private String author;
    private Integer publish_year;
    private String publisher;
    private String cite_str;
    private String team;
    private String detail;
    private String file_path;
    private String upload_user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date upload_time;


    private Integer view_num;
    private Integer download_num;
}
