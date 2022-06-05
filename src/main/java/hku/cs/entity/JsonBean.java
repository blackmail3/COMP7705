package hku.cs.entity;

import lombok.Data;

/**
 * Model Config Entity Class
 * @author xxy
 */
/*
"model_name_or_path": "bert-base-uncased", 用户在创建*任务*时指定，default为"bert-base-uncased"
    "ln_type": "post", 用户在创建*模型*时指定，default为post，可选(post, pre)
    "freeze": "", 用户在创建*模型*时指定，default为""，可选'embeddings', 'encoder', 'all', "1,2,..."
    "cls_type": "fc", 用户在创建*模型*时指定，default为fc，有cnn, lstm, fc
    "pooler_type": "cls", 用户在创建*模型*时指定，default为cls
    "activation": "gelu", 用户在创建*模型*时指定，default为gelu,
    "train_file": "path / to / train set", 必须有
    "valid_file": "path / to / valid set", 可以没有，如果这里没有那么do_eval=false
    "test_file": "path / to / test set", 可以没有
    "max_seq_length": 256, 用户在创建*任务*时给定，default为256
    "pad_to_max_length": false,
    "output_dir": "path / to / output", 后端指定，output存放地址，会包含训练后的模型参数和log
    "overwrite_output_dir": true,
    "num_train_epochs": 10, 用户在创建*任务*时给定，default为10
    "per_device_train_batch_size": 128, 用户在创建*任务*时给定，default为128
    "learning_rate": 5e-5, 用户在创建*任务*时给定，default为5e-5
    "evaluation_strategy": "epoch",
    "load_best_model_at_end": true,
    "do_train": true,
    "do_eval": true,
    "fp16": true,
    "report_to": "wandb",
    "run_name": "任务ID" 后端指定
 */
@Data
public class JsonBean {
    private String model_name_or_path = "bert-base-uncased";
    private String ln_type = "post";
    private String cls_type = "fc";
    private String pooler_type = "cls";
    private String activation = "gelu";
    private String train_file = "";
    private String valid_file = "";
    private String test_file = "";
    private int max_seq_length = 256;
    private boolean pad_to_max_length = false;
    private String output_dir = "";
    private boolean overwrite_output_dir = true;
    private int num_train_epochs = 10;
    private int per_device_train_batch_size = 128;
    private double learning_rate = 5e-5;
    private String evaluation_strategy = "epoch";
    private boolean load_best_model_at_end = true;
    private boolean do_train = true;
    private boolean do_eval = true;
    private boolean fp16 = true;
    private String report_to = "wandb";
    private String run_name = "";   //Task ID

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

    public double getLearning_rate() {
        return learning_rate;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public String getEvaluation_strategy() {
        return evaluation_strategy;
    }

    public void setEvaluation_strategy(String evaluation_strategy) {
        this.evaluation_strategy = evaluation_strategy;
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

    public String getRun_name() {
        return run_name;
    }

    public void setRun_name(String run_name) {
        this.run_name = run_name;
    }
}
