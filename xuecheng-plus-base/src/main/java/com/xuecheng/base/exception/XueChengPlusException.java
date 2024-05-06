package com.xuecheng.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mr.M
 * @version 1.0
 * @description 本项目自定义异常类型
 * @date 2023/2/12 16:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XueChengPlusException extends RuntimeException {

    private String errMessage;
    
    private String errCode;


    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;

    }

    public static void cast(String message){
        throw new XueChengPlusException(message);
    }
    public static void cast(String message,String code){
        throw new XueChengPlusException(message,code);
    }
    public static void cast(CommonError error){
        throw new XueChengPlusException(error.getErrMessage());
    }

}
