package com.zjzy.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author ：admin
 * @date ：Created in 2019/9/27 13:08
 * @description：
 * @modified By：
 * @version: $
 */
// 切面类
@Component
@Aspect
public class Aop {

    // aop 编程里面有几个通知： 前置通知 后置通知 运行通知 异常通知 环绕通知
    // @Before("execution(* com.itmayiedu.service.UserService.add(..))")
    public void before() {
        System.out.println("前置通知 在方法之前执行...");
    }

    // 后置通知 在方法运行后执行
  //  @After("execution(* com.itmayiedu.service.UserService.add(..))")
    public void after() {
        System.out.println("前置通知 在方法之后执行...");
    }

    // 运行通知
  //  @AfterReturning("execution(* com.itmayiedu.service.UserService.add(..))")
    public void returning() {
        System.out.println("运行通知");
    }

    // 异常通知
   // @AfterThrowing("execution(* com.itmayiedu.service.UserService.add(..))")
    public void afterThrowing() {
        System.out.println("异常通知");
    }

    // 环绕通知 在方法之前和之后处理事情
   // @Around("execution(* com.itmayiedu.service.UserService.add(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 调用方法之前执行
        System.out.println("环绕通知 调用方法之前执行");
        proceedingJoinPoint.proceed();// 代理调用方法 注意点： 如果调用方法抛出溢出不会执行后面代码
        // 调用方法之后执行
        System.out.println("环绕通知 调用方法之后执行");
    }
}
