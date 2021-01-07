package jjc.springboot1.util;

/**
 * RESTFUL风格返回对象，可以包含错误信息，可以看做是对数据的再次封装
 */
public class Result {

    private static int SUCCESS_CODE = 0;
    private static int FAIL_CODE = 1;

    int code;
    String message;
    Object data;

    //不可以直接通过new构造该类对象
    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回一个Result对象，只有状态码
     * @return 返回一个Result对象，只有状态码
     */
    public static Result success(){
        return new Result(SUCCESS_CODE, null, null);
    }

    /**
     * 返回一个Result对象，包含状态码和数据
     * @param data 要返回的数据
     * @return 返回一个Result对象，包含状态码和数据
     */
    public static Result success(Object data) {
        return new Result(SUCCESS_CODE,"",data);
    }

    /**
     * 返回一个Result对象，包含状态码和错误信息。
     * @param message 错误信息
     * @return 返回一个Result对象，包含状态码和错误信息。
     */
    public static Result fail(String message) {
        return new Result(FAIL_CODE,message,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
