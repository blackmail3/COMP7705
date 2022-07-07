package hku.cs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    private String taskName;

    private String taskType;

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
