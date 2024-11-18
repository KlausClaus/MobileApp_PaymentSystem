package com.example.payment.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for API response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    /**
     * Creates a success response without any data.
     *
     * @return A {@code Result} object with status code 200 and no data.
     */
    public static Result sucess(){
        return new Result(Constants.CODE_200,"",null);
    }

    /**
     * Creates a success response with data.
     *
     * @param data The data to include in the response.
     * @return A {@code Result} object with status code 200 and the provided data.
     */
    public static Result sucess(Object data){
        return new Result(Constants.CODE_200,"",data);
    }

    /**
     * Creates an error response with a custom status code and message.
     *
     * @param code The error status code.
     * @param msg  The error message.
     * @return A {@code Result} object with the provided error code and message.
     */
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    /**
     * Creates a generic error response with a predefined status code and message.
     *
     * @return A {@code Result} object with status code 500 and a "System error" message.
     */
    public static Result error(){
        return new Result(Constants.CODE_500,"System error",null);
    }

}
