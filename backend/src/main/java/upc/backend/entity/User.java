package upc.backend.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class User {
    private Integer UserId;
    private String UserName;
    private String UserPassword;
    private String NickName;
    private Byte UserLocked;
    private String user_role;
    private String UserEmail;
    private String UserCollege;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date RegisterTime;
}
