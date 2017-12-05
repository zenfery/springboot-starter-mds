package cc.zenfery.boot.autoconfigure.mds.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import cc.zenfery.boot.autoconfigure.mds.annotation.Mds;
import cc.zenfery.boot.autoconfigure.mds.tools.MdsUtils;

public class MdsAnnotationMethodMatcher extends AnnotationMethodMatcher {

    public MdsAnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
        super(annotationType);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Mds mds = MdsUtils.findMdsAnnotation(method, targetClass);
        if (mds != null) {
            return true;
        }

        return false;
    }
}
