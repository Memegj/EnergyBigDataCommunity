package upc.backend.controller.code.Param;

import lombok.Data;

@Data
public class CodeUpdateParam {
    private Integer CodeId;
    private String CodeName;
    private String CodeAbstract;
    private String CodeDetails;
    private String Url;
    private Integer DownloadTimes;
    private Integer TeamId;
}
