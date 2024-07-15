package upc.backend.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Userteam {
    private Integer User_teamId;
    private Integer UserId;
    private Integer TeamId;

}
