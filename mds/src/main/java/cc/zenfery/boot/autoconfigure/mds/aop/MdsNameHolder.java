package cc.zenfery.boot.autoconfigure.mds.aop;

import org.springframework.util.Assert;

/**
 * hold current thread DataSource Name
 * 
 * @author zenfery
 *
 */
public class MdsNameHolder {

    private static final ThreadLocal<String> name = new ThreadLocal<String>();

    public static String getDsname() {
        return name.get();
    }

    public static void setDsname(String dsname) {
        Assert.hasText(dsname, "DataSource Name must be text");
        MdsNameHolder.name.set(dsname);
    }

    public static void clear() {
        MdsNameHolder.name.remove();
    }
}
