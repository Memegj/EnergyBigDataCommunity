package upc.backend.controller.datasets.Param;

import lombok.Data;

@Data
public class DatasetsUpdateParam {
    private Integer DataId;
    private String DataName;
    private String DataAbstract;
    private String DataDetails;
    private Integer isPublic;
    private String Url;
}
