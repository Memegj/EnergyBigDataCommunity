package upc.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
@Setter
@Getter
@Data
public class UserTeam {
    private Integer User_Teamid;
    private Integer TeamId;
    private Integer UserId;

}
