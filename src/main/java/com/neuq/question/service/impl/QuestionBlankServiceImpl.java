package com.neuq.question.service.impl;


import com.neuq.question.data.dao.QuestionBlankRepository;
import com.neuq.question.data.pojo.QuestionBlankDO;
import com.neuq.question.service.QuestionBlankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yegk7
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionBlankServiceImpl implements QuestionBlankService {

    private final QuestionBlankRepository repository;

    @Override
    public List<QuestionBlankDO> getList(int start, int size, Boolean enable, String keyword) {

        return repository.getList(enable, keyword, start, size);
    }

    @Override
    public boolean verifyQuestionBlank(String blankId) {

        return repository.queryEnableById(blankId);
    }
}
