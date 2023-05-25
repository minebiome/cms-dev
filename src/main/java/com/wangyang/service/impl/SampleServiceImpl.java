package com.wangyang.service.impl;

import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.sample.Report;
import com.wangyang.pojo.sample.Sample;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.SampleRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IReportService;
import com.wangyang.service.ISampleService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl extends AbstractCrudService<Sample,Sample, BaseVo,Integer> implements ISampleService {

    SampleRepository sampleRepository;
    public SampleServiceImpl(SampleRepository sampleRepository) {
        super(sampleRepository);
        this.sampleRepository = sampleRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
