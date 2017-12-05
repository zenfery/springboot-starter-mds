package cc.zenfery.boot.autoconfigure.mds.sample.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestDao {
	
	@Select("select name from test where id = 1")
	String test();

}
