package hku.cs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long taskid;

    private String taskname;

    private String tasktype;

    private String description;

    private Long modelId;

    private Long datasetId;

    private Float learningRate;

    private Integer maxLength;

    private Integer epoch;

    private String lossFun;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;


}
