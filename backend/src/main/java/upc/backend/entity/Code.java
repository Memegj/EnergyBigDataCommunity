package upc.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;


@Setter
@Getter
@Data
public class Code {
    private int codeid;
    private String codename;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadtime;
    private String codeabstract;
    private String codedetails;
    private int isPublic;
    private int downloadtimes;
    private int UserId;
    private String url;
    private User user;

    // Getters and setters
}
