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

    @TableId(value = "idmodel", type = IdType.AUTO)
    private Long idmodel;

    private Long userid;

    private String modelname;

    private String preTrainedModel;

    private String basicModel;

    private String config;

    private String description;

    private LocalDateTime updatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getIdmodel() {
        return idmodel;
    }

    public void setIdmodel(Long idmodel) {
        this.idmodel = idmodel;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getPreTrainedModel() {
        return preTrainedModel;
    }

    public void setPreTrainedModel(String preTrainedModel) {
        this.preTrainedModel = preTrainedModel;
    }

    public String getBasicModel() {
        return basicModel;
    }

    public void setBasicModel(String basicModel) {
        this.basicModel = basicModel;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdateTime() {
        return updatetime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updatetime = updateTime;
    }
}
