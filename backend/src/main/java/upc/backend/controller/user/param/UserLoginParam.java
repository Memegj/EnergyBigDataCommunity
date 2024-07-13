package upc.backend.controller.user.param;
import lombok.Data;
import java.io.Serializable;

@Data
public class UserLoginParam implements Serializable {
    private Integer userid;
    private String password;
}
