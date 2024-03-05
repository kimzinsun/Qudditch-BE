package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final TestMapper testMapper;

    @Autowired
    public TestService(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public String test() {
        return testMapper.selectTest();
    }
}
