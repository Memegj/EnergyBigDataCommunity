package upc.backend.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class Teamfile {
    private Integer FileId;
    private Integer DataId;
    private Integer VideoId;
    private Integer LiterId;
    private Integer CodeId;
    private Integer TeamId;
}
