package cc.zenfery.boot.autoconfigure.mds;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = MdsProperties.PREFIX)
@Getter
@Setter
public class MdsProperties implements BeanClassLoaderAware {

    public static final String PREFIX = "spring.mds";

    private boolean enabled;

    private ClassLoader classLoader;

    private List<DataSourceProperties> datasources = new ArrayList<DataSourceProperties>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        if (!datasources.isEmpty()) {
            for (DataSourceProperties dataSource : datasources) {
                dataSource.setBeanClassLoader(classLoader);
            }
        }
    }
}
