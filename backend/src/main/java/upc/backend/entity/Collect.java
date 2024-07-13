package upc.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Collect {
    private Integer CollectId;

    private String  CollectType;
    private Integer UserId;
    private Integer CodeId;
    private Integer LiterId;
    private Integer DataId;
    private Integer VideoId;
    private String DataName;
    private String UploadTime;
    private String TeamName;
    private Integer TeamId;
}
