package upc.backend.controller.student.Param;
import lombok.Data;
@Data
public class StudentAddParam {
    private Integer UserId;
    private String UserName;
    private String UserPassword;
    private String NickName;
    private Byte UserLocked;
    private String user_role;
    private String UserEmail;
    private String UserCollege;
    private Integer TeamId;
    private String TeamName;
}
