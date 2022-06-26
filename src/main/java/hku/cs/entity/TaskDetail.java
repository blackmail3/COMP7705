package hku.cs.entity;

public class TaskDetail {
    String TrainingTime = "";
    Float Accuracy;
    Float Precision;
    Float F1;
    Float Recall;

    public String getTrainingTime() {
        return TrainingTime;
    }

    public void setTrainingTime(String trainingTime) {
        TrainingTime = trainingTime;
    }

    public Float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(Float accuracy) {
        Accuracy = accuracy;
    }

    public Float getPrecision() {
        return Precision;
    }

    public void setPrecision(Float precision) {
        Precision = precision;
    }

    public Float getF1() {
        return F1;
    }

    public void setF1(Float f1) {
        F1 = f1;
    }

    public Float getRecall() {
        return Recall;
    }

    public void setRecall(Float recall) {
        Recall = recall;
    }
}
