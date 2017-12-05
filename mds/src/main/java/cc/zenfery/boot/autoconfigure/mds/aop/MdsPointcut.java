package cc.zenfery.boot.autoconfigure.mds.aop;

import lombok.Getter;
import lombok.Setter;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

@Getter
@Setter
public class MdsPointcut implements Pointcut {

    private MethodMatcher methodMatcher;

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

}
