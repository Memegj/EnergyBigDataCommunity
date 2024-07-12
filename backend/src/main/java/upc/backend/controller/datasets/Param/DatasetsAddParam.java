package upc.backend.controller.datasets.Param;
import lombok.Data;

@Data
public class DatasetsAddParam {
    private String DataName;
    private String DataAbstract;
    private String DataDetails;
    private String Url;
    private Integer TeamId;
    private Integer DownloadTimes;



}
