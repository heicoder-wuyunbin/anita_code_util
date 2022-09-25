package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author [author]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private boolean flag;
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data){
        Result<T> result=new Result<>();
        result.flag=true;
        result.code=20000;
        result.message="success";
        result.data=data;
        return result;
    }

    public static <T> Result<T> error(T data){
        Result<T> result=new Result<>();
        result.flag=false;
        result.code=-1;
        result.message="error";
        result.data=data;
        return result;
    }
}
