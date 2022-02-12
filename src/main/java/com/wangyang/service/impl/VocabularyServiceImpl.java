package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Vocabulary;
import com.wangyang.repository.VocabularyRepository;
import com.wangyang.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VocabularyServiceImpl implements IVocabularyService {

    @Autowired
    VocabularyRepository vocabularyRepository;

    @Override
    public Vocabulary add(Vocabulary vocabulary){
        return vocabularyRepository.save(vocabulary);
    }
}
