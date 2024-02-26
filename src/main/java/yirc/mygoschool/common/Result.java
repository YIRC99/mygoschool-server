package yirc.mygoschool.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private Integer code;
    private String message;
    public Object data;


    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setData(data);
        result.setMessage("成功");
        return result;
    }

    public static Result success(Object data, Integer code) {
        Result result = new Result();
        result.setCode(code);
        result.setData(data);
        result.setMessage("成功");
        return result;
    }

    public static Result error(String message){
        Result result = new Result();
        result.setCode(ResultCode.ERROR);
        result.setData(null);
        result.message = message;
        return result;
    }

    public static Result error(String message, Integer code){
        Result result = new Result();
        result.setCode(code);
        result.setData(null);
        result.message = message;
        return result;
    }

}
