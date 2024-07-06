package upc.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
@Data
@Setter
@Getter
public class InfantMessage{
    //id
    private Integer infantmessageid;

    //"幼儿姓名")
    private String infantname;
    //"昵称")
    private String nickname;
    // "性别")
    private String sex;
    //"年龄")
    private String age;
    // "出生日期")
    private String dateof;
    // "身份证号")
    private String infantidnumber;
    // "民族")
    private String nation;
   // "出生地")
    private String birthplace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentdate;
}