package cc.zenfery.boot.autoconfigure.mds.sample.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.zenfery.boot.autoconfigure.mds.MdsProperties;
import cc.zenfery.boot.autoconfigure.mds.sample.service.TestService;

@RestController
public class TestController {
	
	@Autowired
	private MdsProperties mdsProperties;
	
	@Autowired
	private TestService testService;
	
	@RequestMapping(value="/test-db", method=RequestMethod.GET)
	public String testDb(){
		return testService.test();
	}
	
	@RequestMapping(value="/test-db1", method=RequestMethod.GET)
	public String testDb1(){
		return testService.test1();
	}


}
