package upc.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@Data
public class UploadFile {
    private Integer id;
    private String filename;
    private String upload_user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date upload_time;;
    private String file_path;
    private String file_info;
}
