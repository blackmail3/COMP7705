package hku.cs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xxy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dataset implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long datasetId;

    private String name;

    private String path;

    private Long userid;

    private String description;

    private Integer volume;

    private Integer markingTotal;

    private Integer markingFinish;

    private Integer status;

    private LocalDateTime updateTime;

    private String version;

    private Long datasetGroupId;

    private String type;

    private String label;

    private String input1;

    private String input2;


}