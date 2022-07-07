package hku.cs.entity;

import lombok.Data;

/**
 * Model Config Entity Class
 * @author xxy
 */
/*
{
    "model_name_or_path": "bert-base-uncased",
    "ln_type": "post",
    "freeze_layers": "10,11",
    "freeze": "",
    "cls_type": "fc",
    "pooler_type": "cls",
    "activation": "gelu",
    "train_file": "data/train.csv",
    "valid_file": "data/valid.csv",
    "test_file": "data/test.csv",
    "src_column1": "content",
    "tgt_column": "label",
    "max_seq_length": 256,
    "pad_to_max_length": false,
    "output_dir": "output",
    "overwrite_output_dir": true,
    "num_train_epochs": 1,
    "per_device_train_batch_size": 1,
    "per_device_eval_batch_size": 1,
    "learning_rate": 5e-5,
    "warmup_ratio": 0.1,
    "weight_decay": 0.01,
    "evaluation_strategy": "epoch",
    "save_strategy": "epoch",
    "save_total_limit": 5,
    "load_best_model_at_end": true,
    "do_train": true,
    "do_eval": true,
    "fp16": false,
    "repost_to": "tensorboard",
    "logging_dir": "output/tensorboard",
    "disable_tqdm": true
}
 */
@Data
public class TrainJsonBean {
    private String model_name_or_path = "bert-base-uncased";
    private String ln_type = "post";
    private String freeze_layer = "";
    private String freeze = "";
    private String cls_type = "fc";
    private String pooler_type = "cls";
    private String activation = "gelu";
    private String train_file = "";
    private String valid_file = "";
    private String test_file = "";
    private String src_column1 = "";
    private String src_column2 = "";
    private String tgt_column = "";
    private int max_seq_length = 256;
    private boolean pad_to_max_length = false;
    private String output_dir = "";
    private boolean overwrite_output_dir = true;
    private int num_train_epochs = 10;
    private int per_device_train_batch_size = 128;
    private int per_device_eval_batch_size = 128;
    private double learning_rate = 5e-5;
    private double warmup_ratio =0.1;
    private double weight_decay=0.01;
    private String evaluation_strategy = "epoch";
    private String save_strategy = "epoch";
    private int save_total_limit = 5;
    private boolean load_best_model_at_end = true;
    private boolean do_train = true;
    private boolean do_eval = true;
    private boolean fp16 = true;
    private String report_to = "tensorboard";
    private String logging_dir = "";
    private boolean disable_tqdm = true;

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

    public String getTrain_file() {
        return train_file;
    }

    public void setTrain_file(String train_file) {
        this.train_file = train_file;
    }

    public String getValid_file() {
        return valid_file;
    }

    public void setValid_file(String valid_file) {
        this.valid_file = valid_file;
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

    public int getNum_train_epochs() {
        return num_train_epochs;
    }

    public void setNum_train_epochs(int num_train_epochs) {
        this.num_train_epochs = num_train_epochs;
    }

    public int getPer_device_train_batch_size() {
        return per_device_train_batch_size;
    }

    public void setPer_device_train_batch_size(int per_device_train_batch_size) {
        this.per_device_train_batch_size = per_device_train_batch_size;
    }

    public int getPer_device_eval_batch_size() {
        return per_device_eval_batch_size;
    }

    public void setPer_device_eval_batch_size(int per_device_eval_batch_size) {
        this.per_device_eval_batch_size = per_device_eval_batch_size;
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public double getWarmup_ratio() {
        return warmup_ratio;
    }

    public void setWarmup_ratio(double warmup_ratio) {
        this.warmup_ratio = warmup_ratio;
    }

    public double getWeight_decay() {
        return weight_decay;
    }

    public void setWeight_decay(double weight_decay) {
        this.weight_decay = weight_decay;
    }

    public String getEvaluation_strategy() {
        return evaluation_strategy;
    }

    public void setEvaluation_strategy(String evaluation_strategy) {
        this.evaluation_strategy = evaluation_strategy;
    }

    public String getSave_strategy() {
        return save_strategy;
    }

    public void setSave_strategy(String save_strategy) {
        this.save_strategy = save_strategy;
    }

    public int getSave_total_limit() {
        return save_total_limit;
    }

    public void setSave_total_limit(int save_total_limit) {
        this.save_total_limit = save_total_limit;
    }

    public boolean isLoad_best_model_at_end() {
        return load_best_model_at_end;
    }

    public void setLoad_best_model_at_end(boolean load_best_model_at_end) {
        this.load_best_model_at_end = load_best_model_at_end;
    }

    public boolean isDo_train() {
        return do_train;
    }

    public void setDo_train(boolean do_train) {
        this.do_train = do_train;
    }

    public boolean isDo_eval() {
        return do_eval;
    }

    public void setDo_eval(boolean do_eval) {
        this.do_eval = do_eval;
    }

    public boolean isFp16() {
        return fp16;
    }

    public void setFp16(boolean fp16) {
        this.fp16 = fp16;
    }

    public String getReport_to() {
        return report_to;
    }

    public void setReport_to(String report_to) {
        this.report_to = report_to;
    }

    public String getLogging_dir() {
        return logging_dir;
    }

    public void setLogging_dir(String logging_dir) {
        this.logging_dir = logging_dir;
    }

    public boolean isDisable_tqdm() {
        return disable_tqdm;
    }

    public void setDisable_tqdm(boolean disable_tqdm) {
        this.disable_tqdm = disable_tqdm;
    }
}
