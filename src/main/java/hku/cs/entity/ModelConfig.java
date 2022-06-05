package hku.cs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ModelConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long modelId;

    private String lnType = "post";

    private String freeze = "";

    private String clsType = "fc";

    private String poolerType = "cls";

    private String activation = "gelu";


}