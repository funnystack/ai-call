package com.funny.combo.ai.demo.web.interceptor;

import com.funny.combo.ai.demo.common.BaseResult;
import com.funny.combo.ai.demo.common.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 *
 * @Author funnystack
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class BootExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public BaseResult<?> handleRRException(BizException e) {
        log.error(e.getMessage(), e);
        return BaseResult.error(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResult<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResult<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return BaseResult.error("数据库中已存在该记录");
    }


    @ExceptionHandler(Exception.class)
    public BaseResult<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.error("操作失败，" + e.getMessage());
    }

    /**
     * @param e
     * @return
     * @Author 政辉
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResult<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuffer sb = new StringBuffer();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.error(sb.toString(), e);
        //return BaseResult.error("没有权限，请联系管理员授权");
        return BaseResult.error(405, sb.toString());
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return BaseResult.error("文件大小超出10MB限制, 请压缩或降低文件质量! ");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public BaseResult<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return BaseResult.error("字段太长,超出数据库字段的长度");
    }

    @ExceptionHandler(PoolException.class)
    public BaseResult<?> handlePoolException(PoolException e) {
        log.error(e.getMessage(), e);
        return BaseResult.error("Redis 连接异常!");
    }

}
