package cc.zenfery.boot.autoconfigure.mds.tools;

import java.lang.reflect.Method;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import cc.zenfery.boot.autoconfigure.mds.annotation.Mds;

@CommonsLog
public abstract class MdsUtils {

    /**
     * Fetch the Mds Annotation From the Method Given.
     * 
     * @param method
     * @param targetClass
     * @return
     */
    public static Mds findMdsAnnotation(Method method, Class<?> targetClass) {
        boolean isDebug = log.isDebugEnabled();
        Mds mds;

        // Method find
        if ((mds = AnnotationUtils.findAnnotation(method, Mds.class)) != null) {
            if (isDebug) {
                log.debug("Method " + targetClass.getSimpleName() + ".{" + method.getName()
                        + "} has @DataSource(value=" + mds.value() + ")");
            }
            return mds;
        }

        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        if ((mds = AnnotationUtils.findAnnotation(specificMethod, Mds.class)) != null) {
            if (isDebug) {
                log.debug("Method " + targetClass.getSimpleName() + ".{" + method.getName()
                        + "} has @DataSource(value=" + mds.value() + ")");
            }
            return mds;
        }

        // Class
        if ((mds = AnnotationUtils.findAnnotation(targetClass, Mds.class)) != null) {
            if (isDebug) {
                log.debug("Method(Class) " + targetClass.getSimpleName() + ".{" + method.getName()
                        + "} has @DataSource(value=" + mds.value() + ")");
            }
            return mds;
        }
        return null;
    }
}
