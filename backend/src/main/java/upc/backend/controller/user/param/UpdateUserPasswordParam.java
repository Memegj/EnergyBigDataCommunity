package upc.backend.controller.user.param;
import lombok.Data;

@Data
public class UpdateUserPasswordParam {
    private String originalPassword;
    private String newPassword;
    private String token;
}
