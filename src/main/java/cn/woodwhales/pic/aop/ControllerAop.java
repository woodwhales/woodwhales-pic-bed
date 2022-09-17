package cn.woodwhales.pic.aop;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.woodwhales.pic.util.IpTool.getIpAddress;

/**
 * @author woodwhales
 */
@Slf4j
@Aspect
@Component
public class ControllerAop {

    @Pointcut("execution (* cn.woodwhales.pic.controller..*.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object providerAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Thread.currentThread().setName(UUID.randomUUID().toString(true));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringType()
                .getName();
        String fullMethodName = declaringTypeName + "#" + signature.getName();
        StringBuilder requestParamBuilder = new StringBuilder();
        String clientIP;
        String requestUrl = null;
        HttpServletRequest request;
        try {
            log.info("======================= {} start =======================", fullMethodName);
            request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            clientIP = getIpAddress(request);
            String methodType = request.getMethod();
            requestUrl = String.format("[ %s ] %s", methodType, request.getRequestURL()
                    .toString());
            // 请求地址
            log.info("request_url={}", requestUrl);
            // 请求ip
            log.info("request_ip={}", clientIP);
            // 请求参数
            log.info("request_param={}", requestParamBuilder.append(getParamStringFromRequest(request)));
            if (!StringUtils.equals(RequestMethod.GET.name(), methodType)) {
                Object requestBodyParam = collectRequestBodyParam(joinPoint.getArgs(), signature);
                if (null != requestBodyParam) {
                    log.info("request_requestBody={}", requestParamBuilder.append(JSON.toJSONString(requestBodyParam)));
                }
            }
            //请求头信息
            log.info("X-AUTH-TOKEN={}", request.getHeader("X-AUTH-TOKEN"));
            request.setAttribute("traceId", Thread.currentThread().getName());
            Object result = joinPoint.proceed();
            if (null != result) {
                log.info("request_responseBody={}", JSON.toJSONString(result));
            }
            return result;
        } catch (Throwable throwable) {
            String requestParam = requestParamBuilder.toString();
            log.error("system happen error {}, requestParam : {}", requestUrl, requestParam, throwable);
            throw throwable;
        } finally {
            long costTime = stopWatch.getTotalTimeMillis();
            log.info("requestUrl = {}, consume : {} ms", requestUrl, costTime);
            log.info("======================= {} end =======================", fullMethodName);
        }
    }

    private Object collectRequestBodyParam(Object[] args, Signature signature) {
        Method method = ((MethodSignature)signature).getMethod();
        Parameter[] parameters = method.getParameters();
        return Arrays.stream(parameters)
                .filter(parameter -> null != parameter.getAnnotation(RequestBody.class))
                .map(parameter -> ArrayUtils.indexOf(parameters, parameter))
                .map(index -> args[index])
                .findFirst()
                .orElse(null);
    }

    private String getParamStringFromRequest(final HttpServletRequest request) {
        return parseEnumerationToString(request.getParameterNames(), request::getParameter);
    }

    private String getHeaderStringFromRequest(final HttpServletRequest request) {
        return parseEnumerationToString(request.getHeaderNames(), request::getHeader);
    }

    private String parseEnumerationToString(Enumeration<String> enumerations, Function<String, String> function) {
        List<String> list = ListUtils.emptyIfNull(EnumerationUtils.toList(enumerations));
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(Function.identity(), function));
        return Joiner.on("&")
                .withKeyValueSeparator("=")
                .join(map);
    }

}