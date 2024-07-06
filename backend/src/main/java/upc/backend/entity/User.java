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
    private Integer userid;
    private String username;
    private String password;
    private String nickname;
    private Byte locked;
    private Byte deleted;
    private String user_role;
    private String team;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date register_time;
}
