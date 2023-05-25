package com.wangyang.service.impl;

import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.survey.Answers;
import com.wangyang.pojo.survey.Questions;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.QuestionsRepository;
import com.wangyang.repository.base.BaseRepository;
import com.wangyang.service.IAnswersService;
import com.wangyang.service.IQuestionsService;
import com.wangyang.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class QuestionsServiceImpl extends AbstractCrudService<Questions,Questions, BaseVo,Integer> implements IQuestionsService {


    QuestionsRepository questionsRepository;
    public QuestionsServiceImpl(QuestionsRepository questionsRepository) {
        super(questionsRepository);
        this.questionsRepository = questionsRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
