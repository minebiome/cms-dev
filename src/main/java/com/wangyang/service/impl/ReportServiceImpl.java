package com.wangyang.service.impl;

import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.sample.Report;
import com.wangyang.pojo.survey.Questions;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.ReportRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IQuestionsService;
import com.wangyang.service.IReportService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends AbstractCrudService<Report,Report, BaseVo,Integer> implements IReportService {

    ReportRepository reportRepository;
    public ReportServiceImpl(ReportRepository reportRepository) {
        super(reportRepository);
        this.reportRepository = reportRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
