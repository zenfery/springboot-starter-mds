package cc.zenfery.boot.autoconfigure.mds.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.zenfery.boot.autoconfigure.mds.annotation.Mds;
import cc.zenfery.boot.autoconfigure.mds.sample.dao.TestDao;
import cc.zenfery.boot.autoconfigure.mds.sample.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public String test() {
        return testDao.test();
    }

    @Mds("mds1")
    @Override
    public String test1() {
        return testDao.test();
    }

}
