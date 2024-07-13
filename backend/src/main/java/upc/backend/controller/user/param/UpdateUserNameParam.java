package upc.backend.controller.user.param;
import lombok.Data;

@Data
public class UpdateUserNameParam {
    private String nickname;
    private String useremail;
    private String usercollege;
    private String token;
}
