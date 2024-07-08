package upc.backend.controller.user.param;
import lombok.Data;
import java.io.Serializable;

@Data
public class UserAddParam implements Serializable {
    private Integer UserId;
    private String UserName;
    private String UserPassword;
    private String NickName;
    private String user_role;
    private String UserEmail;
    private String UserCollege;
}
