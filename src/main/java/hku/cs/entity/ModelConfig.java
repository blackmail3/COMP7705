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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getLnType() {
        return lnType;
    }

    public void setLnType(String lnType) {
        this.lnType = lnType;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getClsType() {
        return clsType;
    }

    public void setClsType(String clsType) {
        this.clsType = clsType;
    }

    public String getPoolerType() {
        return poolerType;
    }

    public void setPoolerType(String poolerType) {
        this.poolerType = poolerType;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}