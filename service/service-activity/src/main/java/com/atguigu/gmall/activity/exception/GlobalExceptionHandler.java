package com.atguigu.gmall.activity.exception;


import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理程序
 * 1、所有的业务异常都是一个异常 throw new GmallException(业务错误码);
 * 2、系统其它异常
 *
 * @author zhangjuyi
 * @date 2022/06/22
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 业务异常就直接根据业务码响应错误
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    public Result handleBizException(GmallException e){
        Result<String> result = new Result<>();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        result.setData("");
        return result;
    }


    /**
     * 系统其它异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleOtherException(Exception e){
        Result<Object> fail = Result.fail();
        fail.setMessage(e.getMessage());
        return fail;
    }



}
