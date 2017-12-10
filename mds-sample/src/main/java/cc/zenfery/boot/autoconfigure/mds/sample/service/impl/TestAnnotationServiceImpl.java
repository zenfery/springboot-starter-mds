package cc.zenfery.boot.autoconfigure.mds.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.zenfery.boot.autoconfigure.mds.annotation.Mds;
import cc.zenfery.boot.autoconfigure.mds.sample.common.annotation.Mds1DataSource;
import cc.zenfery.boot.autoconfigure.mds.sample.dao.TestDao;
import cc.zenfery.boot.autoconfigure.mds.sample.service.TestAnnotationService;

@Service
@Mds("mds")
public class TestAnnotationServiceImpl implements TestAnnotationService {

    @Autowired
    private TestDao testDao;

    @Override
    public String test() {
        return testDao.test();
    }

    @Mds1DataSource
    @Override
    public String test1() {
        return testDao.test();
    }

}
