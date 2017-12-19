package cc.zenfery.boot.autoconfigure.mds.aop;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import cc.zenfery.boot.autoconfigure.mds.annotation.Mds;
import cc.zenfery.boot.autoconfigure.mds.tools.MdsUtils;

@CommonsLog
@Getter
@Setter
public class MdsAdvice implements MethodInterceptor, Ordered {

    private int order;

    private String defaultDataSourceName;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method invocationMethod = invocation.getMethod();

        if (log.isDebugEnabled()) {
            log.debug(" ==> Multi DataSource advice execute.");
        }
        Mds mds = MdsUtils.findMdsAnnotation(invocationMethod, invocation.getThis().getClass());
        String currentDataSourceName = MdsNameHolder.getDsname();
        String currentSettingDataSourceName;
        if (mds != null) {

            currentSettingDataSourceName = mds.value();

            if (StringUtils.isEmpty(currentSettingDataSourceName)) {
                currentSettingDataSourceName = defaultDataSourceName;
            }
        } else {
            currentSettingDataSourceName = defaultDataSourceName;
        }

        if (!StringUtils.isEmpty(currentSettingDataSourceName)
                && !currentSettingDataSourceName.equals(currentDataSourceName)) {
            MdsNameHolder.setDsname(currentSettingDataSourceName);
            log.debug("'" + currentSettingDataSourceName + "' changed dataSource");
        }

        // invoke
        try {
            return invocation.proceed();
        } finally {
            MdsNameHolder.clear();
            if (log.isDebugEnabled()) {
                log.debug("Methods \"" + invocationMethod + "\" annotated with @Datasource is end");
            }
        }
    }
}
