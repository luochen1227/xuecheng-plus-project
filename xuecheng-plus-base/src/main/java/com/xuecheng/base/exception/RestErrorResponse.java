package com.xuecheng.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Mr.M
 * @version 1.0
 * @description 和前端约定返回的异常信息模型
 * @date 2023/2/12 16:55
 */
@Data

@NoArgsConstructor

public class RestErrorResponse implements Serializable {

 private String errMessage;
 private String errCode;

 public RestErrorResponse(String errMessage){
  this.errMessage= errMessage;
 }

 public RestErrorResponse(String errMessage, String errCode) {
  this.errMessage = errMessage;
  this.errCode = errCode;
 }
}
