package com.wangyang.service.impl;

import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.sample.Sample;
import com.wangyang.pojo.survey.Surveys;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.SurveysRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.ISampleService;
import com.wangyang.service.ISurveysService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class SurveysServiceImpl extends AbstractCrudService<Surveys,Surveys, BaseVo,Integer> implements ISurveysService {

    SurveysRepository surveysRepository;
    public SurveysServiceImpl(SurveysRepository surveysRepository) {
        super(surveysRepository);
        this.surveysRepository = surveysRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
