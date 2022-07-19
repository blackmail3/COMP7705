package hku.cs.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("task_id")
    private Long taskId;

    private String taskName;

    @ApiModelProperty("'training':0/'predict':1/'eval':2")
    private int taskType;

    private String description;

    private Long modelId;

    private Long datasetIdTrain;

    private Long datasetIdTest;

    private Float learningRate;

    private Integer maxLength;

    private Integer epoch;

    private String lossFun;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ApiModelProperty("0:Not Start 1:Running 2:Finish 3:Fail")
    private Integer status;

    private Long userId;

    private int batchSize;


}
