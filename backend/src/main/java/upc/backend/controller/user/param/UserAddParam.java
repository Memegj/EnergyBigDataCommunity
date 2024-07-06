package upc.backend.controller.user.param;
import lombok.Data;
import java.io.Serializable;

@Data
public class UserAddParam implements Serializable {
    private String username;
    private String nickname;
    private String password;
    private String user_role;
}
