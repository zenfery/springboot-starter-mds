package cc.zenfery.boot.autoconfigure.mds;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import cc.zenfery.boot.autoconfigure.mds.aop.MdsNameHolder;


public class MdsRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return MdsNameHolder.getDsname();
	}

}
