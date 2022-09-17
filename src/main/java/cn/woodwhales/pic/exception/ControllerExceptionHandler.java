package cn.woodwhales.pic.exception;

import cn.woodwhales.common.model.vo.RespVO;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author woodwhales
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public RespVO<Void> exception(Exception exception) {
		log.error("{}", exception.getMessage(), exception);
		return RespVO.errorWithErrorMsg("请求非法");
	}
	
	@ResponseBody
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public RespVO<Void> exception(MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		log.error("{}", errorMessage);
		return RespVO.errorWithErrorMsg(errorMessage);
	}

}
