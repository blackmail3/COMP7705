package hku.cs.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xxy
 */
@ApiModel("Dataset")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dataset implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("NOT necessary")
    @TableId("dataset_id")
    private Long datasetId;

    private String name;

    @ApiModelProperty("NOT necessary")
    private String path;

    @ApiModelProperty("NOT necessary")
    private Long userId;

    private String description;

    @ApiModelProperty("NOT necessary")
    private Integer volume;

    @ApiModelProperty("NOT necessary")
    private Integer markingTotal;

    @ApiModelProperty("NOT necessary")
    private Integer markingFinish;

    @ApiModelProperty("NOT necessary")
    private Integer status;

    private LocalDateTime updateTime;

    @ApiModelProperty("'single':0/'dual':1")
    private int type;

    private String label;

    private String input1;

    private String input2;


}