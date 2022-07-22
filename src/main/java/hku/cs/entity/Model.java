package hku.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Model Entity Class
 * @author xxy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    private Long userId;

    private String modelName;

    private String basicModel;

    private String description;

    private LocalDateTime updateTime;

    private String initParam;

    private String preTrainedModel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBasicModel() {
        return basicModel;
    }

    public void setBasicModel(String basicModel) {
        this.basicModel = basicModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getInitParam() {
        return initParam;
    }

    public void setInitParam(String initParam) {
        this.initParam = initParam;
    }
}
