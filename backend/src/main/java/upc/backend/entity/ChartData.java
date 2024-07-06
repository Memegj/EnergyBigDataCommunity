package upc.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class ChartData {
    private Integer id;
    private String name;
    private String week;
    private Integer value;
}
