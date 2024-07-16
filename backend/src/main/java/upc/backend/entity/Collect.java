package upc.backend.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Collect {
    private Integer CollectId;
    private String CollectType;
    private Integer DataId;
    private Integer VideoId;
    private Integer LiterId;
    private Integer CodeId;
    private Integer UserId;
}
