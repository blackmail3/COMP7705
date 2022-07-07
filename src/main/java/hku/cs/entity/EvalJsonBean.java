package hku.cs.entity;

/*
{
    "model_name_or_path": "output/checkpoint-3",
    "ln_type": "post",
    "freeze_layer": "10,11",
    "freeze": "",
    "cls_type": "fc",
    "pooler_type": "cls",
    "activation": "gelu",
    "test_file": "data/test.csv",
    "src_column1": "content",
    "max_seq_length": 256,
    "pad_to_max_length": false,
    "output_dir": "output",
    "overwrite_output_dir": true,
    "per_device_eval_batch_size": 1,
    "tgt_column": "label"
}
 */
public class EvalJsonBean {
    private String model_name_or_path = "";
    private String ln_type = "post";
    private String freeze_layer = "";
    private String freeze = "";
    private String cls_type = "fc";
    private String pooler_type = "cls";
    private String activation = "gelu";
    private String test_file = "";
    private String src_column1 = "";
    private String tgt_column = "";
    private int max_seq_length = 256;
    private boolean pad_to_max_length = false;
    private String output_dir = "";
    private boolean overwrite_output_dir = true;
    private int per_device_eval_batch_size = 128;

    public String getModel_name_or_path() {
        return model_name_or_path;
    }

    public void setModel_name_or_path(String model_name_or_path) {
        this.model_name_or_path = model_name_or_path;
    }

    public String getLn_type() {
        return ln_type;
    }

    public void setLn_type(String ln_type) {
        this.ln_type = ln_type;
    }

    public String getFreeze_layer() {
        return freeze_layer;
    }

    public void setFreeze_layer(String freeze_layer) {
        this.freeze_layer = freeze_layer;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getCls_type() {
        return cls_type;
    }

    public void setCls_type(String cls_type) {
        this.cls_type = cls_type;
    }

    public String getPooler_type() {
        return pooler_type;
    }

    public void setPooler_type(String pooler_type) {
        this.pooler_type = pooler_type;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getTest_file() {
        return test_file;
    }

    public void setTest_file(String test_file) {
        this.test_file = test_file;
    }

    public String getSrc_column1() {
        return src_column1;
    }

    public void setSrc_column1(String src_column1) {
        this.src_column1 = src_column1;
    }

    public String getTgt_column() {
        return tgt_column;
    }

    public void setTgt_column(String tgt_column) {
        this.tgt_column = tgt_column;
    }

    public int getMax_seq_length() {
        return max_seq_length;
    }

    public void setMax_seq_length(int max_seq_length) {
        this.max_seq_length = max_seq_length;
    }

    public boolean isPad_to_max_length() {
        return pad_to_max_length;
    }

    public void setPad_to_max_length(boolean pad_to_max_length) {
        this.pad_to_max_length = pad_to_max_length;
    }

    public String getOutput_dir() {
        return output_dir;
    }

    public void setOutput_dir(String output_dir) {
        this.output_dir = output_dir;
    }

    public boolean isOverwrite_output_dir() {
        return overwrite_output_dir;
    }

    public void setOverwrite_output_dir(boolean overwrite_output_dir) {
        this.overwrite_output_dir = overwrite_output_dir;
    }

    public int getPer_device_eval_batch_size() {
        return per_device_eval_batch_size;
    }

    public void setPer_device_eval_batch_size(int per_device_eval_batch_size) {
        this.per_device_eval_batch_size = per_device_eval_batch_size;
    }
}
