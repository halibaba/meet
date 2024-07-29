package com.meet.pub.common;/**
 * <p>
 *
 * </p>
 *
 * @author meet
 * @since 2022/11/30
 */

import com.meet.pub.annotation.RepeatSubmit;
import com.meet.pub.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author: mt
 * @description: TODO
 * @date: 2022/11/30 1:37 下午
 */
@Slf4j
@Component
@Aspect
public class NoRepeatSubmitAspect {

	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 定义切点
	 */
	@Pointcut("@annotation(com.meet.pub.annotation.RepeatSubmit)")
	public void preventDuplication() {}

	@Around("preventDuplication()")
	public Object around(ProceedingJoinPoint joinPoint) throws Exception {

		/**
		 * 获取请求信息
		 */
		HttpServletRequest request = WebUtils.getRequest();
//		Enumeration headerNames = request.getHeaderNames();
//		HashMap<String, Object> map = new HashMap<>();
//		while (headerNames.hasMoreElements()) {
//			String key = (String) headerNames.nextElement();
//			String value = request.getHeader(key);
//			System.out.println("=======车机返回Map:"+value+"========");
//			map.put(key, value);
//		}

		// 获取执行方法
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

		//获取防重复提交注解
		RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);

		// 获取token以及方法标记，生成redisKey和redisValue
		String token = request.getHeader("authorization");

		String url = request.getRequestURI();

		/**
		 *  通过前缀 + url + token + 函数参数签名 来生成redis上的 key
		 *
		 */
		String redisKey = url
				.concat(":")
				.concat(token)
				.concat(":");

		// 这个值只是为了标记，不重要
		String redisValue = redisKey.concat(annotation.value()).concat(":submit duplication");

		if (!redisTemplate.hasKey(redisKey)) {
			// 设置防重复操作限时标记(前置通知)
			redisTemplate.opsForValue().set(redisKey, redisValue, annotation.expireSeconds(), TimeUnit.SECONDS);
			try {
				//正常执行方法并返回
				//ProceedingJoinPoint类型参数可以决定是否执行目标方法，
				// 且环绕通知必须要有返回值，返回值即为目标方法的返回值
				return joinPoint.proceed();
			} catch (Throwable throwable) {
				//确保方法执行异常实时释放限时标记(异常后置通知)
				redisTemplate.delete(redisKey);
				throw new RuntimeException(throwable);
			}
		} else {
			// 重复提交了抛出异常，如果是在项目中，根据具体情况处理。
			throw new RuntimeException("请勿重复提交");
		}


	}
}
