package upc.backend.controller.code.Param;
import lombok.Data;

@Data
public class CodeAddParam {
    private String CodeName;
    private String CodeAbstract;
    private String CodeDetails;
    private String Url;
    private Integer TeamId;
    private Integer DownloadTimes;



}
