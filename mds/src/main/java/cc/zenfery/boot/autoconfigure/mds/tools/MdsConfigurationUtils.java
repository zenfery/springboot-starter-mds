package cc.zenfery.boot.autoconfigure.mds.tools;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DatabaseDriver;

import cc.zenfery.boot.autoconfigure.mds.MdsAutoConfiguration;
import cc.zenfery.boot.autoconfigure.mds.SingleDataSourceProperties;

/**
 * Actual DataSource configurations imported by {@link MdsAutoConfiguration}.
 * 
 * @author zenfery
 *
 */
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

        // TODO: bind properties
        dataSource.setTestWhileIdle(true);

        return dataSource;
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
            }
        }
        return null;
    }
}
