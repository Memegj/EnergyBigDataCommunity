package upc.backend.common;
import lombok.Data;
import java.io.Serializable;

@Data
public class BatchIdParam implements Serializable {
    //id数组
    Integer[] ids;
    Integer[] teamId;
}
