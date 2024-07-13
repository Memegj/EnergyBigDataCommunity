package upc.backend.controller.team.param;
import lombok.Data;

@Data
public class TeamUpdateParam {
    private Integer TeamId;
    private String TeamName;
    private String TeamIntro;
    private String RegisterTime;
}
