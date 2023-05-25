package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Tags;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.survey.Answers;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.AnswersRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IAnswersService;
import com.wangyang.service.ITagsService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class AnswersServiceImpl extends AbstractCrudService<Answers,Answers, BaseVo,Integer> implements IAnswersService {

    AnswersRepository answersRepository;
    public AnswersServiceImpl(AnswersRepository answersRepository) {
        super(answersRepository);
        this.answersRepository = answersRepository;

    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
