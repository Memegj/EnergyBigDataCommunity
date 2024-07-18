package upc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wms
 * @since 2024-07-10
 */
@Data
//@EqualsAndHashCode(callSuper = false)
@ApiModel(value="News对象", description="")
public class News implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    @TableField("Title")
    private String title;

    @TableField("ReleaseAuthor")
    private String releaseauthor;

    @TableField("ReleaseTime")
    private String releasetime;

    @TableField("Content")
    private String content;

    @TableField("UserId")
    private Integer userid;

    @TableField("Url")
    private String url;
    @TableField("UploadTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadtime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
