package hku.cs.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private String code;
    private String msg;
    private Object data;
    public static Result succ(Object data) {
        Result m = new Result();
        m.setCode("0");
        m.setData(data);
        m.setMsg("success");
        return m;
    }
    public static Result succ(String mess, Object data) {
        Result m = new Result();
        m.setCode("0");
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
    public static Result fail(String mess) {
        Result m = new Result();
        m.setCode("-1");
        m.setData(null);
        m.setMsg(mess);
        return m;
    }
    public static Result fail(String mess, Object data) {
        Result m = new Result();
        m.setCode("-1");
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
    public static Result fail(int code, String mess, Object data) {
        Result m = new Result();
        m.setCode(""+code);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
}
