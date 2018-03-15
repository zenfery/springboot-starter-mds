package cc.zenfery.boot.autoconfigure.mds.tools;

import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.validation.BindException;

import cc.zenfery.boot.autoconfigure.mds.MdsAutoConfiguration;
import cc.zenfery.boot.autoconfigure.mds.SingleDataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Actual DataSource configurations imported by {@link MdsAutoConfiguration}.
 * 
 * @author zenfery
 *
 */
@CommonsLog
public abstract class MdsConfigurationUtils {

    @SuppressWarnings("unchecked")
    private static <T> T createDataSource(SingleDataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    private static org.apache.tomcat.jdbc.pool.DataSource createTomcatDataSource(SingleDataSourceProperties properties) {

        org.apache.tomcat.jdbc.pool.DataSource dataSource = createDataSource(properties,
                org.apache.tomcat.jdbc.pool.DataSource.class);
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }

        // bind properties
        bindPropertiesForDataSource(dataSource, properties.getTomcat());

        return dataSource;
    }

    private static HikariDataSource createHikariDataSource(SingleDataSourceProperties properties) {
        HikariDataSource hikariDataSource = createDataSource(properties, HikariDataSource.class);
        // bind properties
        bindPropertiesForDataSource(hikariDataSource, properties.getHikari());
        return hikariDataSource;
    }

    private static org.apache.commons.dbcp.BasicDataSource createDbcpDataSource(SingleDataSourceProperties properties) {

        org.apache.commons.dbcp.BasicDataSource dataSource = createDataSource(properties,
                org.apache.commons.dbcp.BasicDataSource.class);
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }

        // bind properties
        bindPropertiesForDataSource(dataSource, properties.getDbcp());

        return dataSource;
    }

    private static org.apache.commons.dbcp2.BasicDataSource createDbcp2DataSource(SingleDataSourceProperties properties) {
        org.apache.commons.dbcp2.BasicDataSource dataSource = createDataSource(properties,
                org.apache.commons.dbcp2.BasicDataSource.class);

        // bind properties
        bindPropertiesForDataSource(dataSource, properties.getDbcp2());

        return dataSource;
    }

    /*
     * Bind The Properties to DataSource 将从配置文件读取的配置设置到DataSource
     */
    private static void bindPropertiesForDataSource(DataSource dataSource, Properties properties) {

        if (dataSource != null && properties != null && properties.size() > 0) {
            //
            PropertiesConfigurationFactory<DataSource> factory = new PropertiesConfigurationFactory<DataSource>(
                    dataSource);
            factory.setConversionService(new DefaultConversionService());

            PropertiesPropertySource pps = new PropertiesPropertySource("DataSource-Pool-Private-Config", properties);
            MutablePropertySources mps = new MutablePropertySources();
            mps.addLast(pps);
            factory.setPropertySources(mps);

            try {
                factory.bindPropertiesToTarget();
            } catch (BindException e) {
                throw new BeanCreationException("MultiDataSourceProperties", "Could not bind properties to "
                        + dataSource.getClass(), e);
            }
        }
    }

    public static boolean isAvailable(SingleDataSourceProperties properties) {
        if (properties != null && properties.getUrl() != null) {
            return true;
        }
        return false;
    }

    public static javax.sql.DataSource crateDataSource(SingleDataSourceProperties properties) {

        if (isAvailable(properties)) {

            // ==> Tomcat
            if (properties.getType() == null || properties.getType() == org.apache.tomcat.jdbc.pool.DataSource.class) {
                return createTomcatDataSource(properties);
            } else if (properties.getType() == HikariDataSource.class) {
                return createHikariDataSource(properties);
            } else if (properties.getType() == org.apache.commons.dbcp.BasicDataSource.class) {
                return createDbcpDataSource(properties);
            } else if (properties.getType() == org.apache.commons.dbcp2.BasicDataSource.class) {
                return createDbcp2DataSource(properties);
            } else {
                log.error(" *** This [" + properties.getType().getName() + "] is not supported..");
            }
        }
        return null;
    }
}
