package upc.backend.controller.user.param;
import lombok.Data;

@Data
public class UpdateUserNameParam {
    private String username;
    private String nickname;
    private String token;
}
