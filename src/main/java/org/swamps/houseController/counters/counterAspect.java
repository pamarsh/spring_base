package org.swamps.houseController.counters;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class counterAspect {

    @Autowired
    private StatisticsCounterService counterService ;

    @Before("execution(public * *(..)) && @annotation(org.swamps.houseController.counters.Counted)")
    public void tick(JoinPoint call) throws Throwable {
        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        String counterKey = method.getAnnotation(Counted.class).value();
        counterService.tick(counterKey);
    }



}
