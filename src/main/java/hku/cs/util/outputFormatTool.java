package hku.cs.util;

import java.awt.*;

public class outputFormatTool {
    private String path;
    private int label;
    private int input1;
    private int input2;

    public outputFormatTool(String path, int label, int input1) {
        this.path = path;
        this.label = label;
        this.input1 = input1;
    }

    public outputFormatTool(String path, int label, int input1, int input2) {
        this.path = path;
        this.label = label;
        this.input1 = input1;
        this.input2 = input2;
    }



    public outputFormatTool(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
