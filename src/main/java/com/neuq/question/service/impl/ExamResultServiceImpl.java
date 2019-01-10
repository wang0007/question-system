package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ExamResultRepository;
import com.neuq.question.data.pojo.ExamResultDO;
import com.neuq.question.domain.enums.ExamType;
import com.neuq.question.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangshyi
 * @date 2018/12/14  15:34
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamResultRepository examResultRepository;

    @Override
    public Long queryUserMinTime(String examId, String memberId) {

        List<ExamResultDO> userAllResult = examResultRepository.queryExamResultList(examId, memberId);

        List<Long> userTimeList = userAllResult.stream()
                .map(ExamResultDO::getTotalTime)
                .collect(Collectors.toList());

        return Collections.min(userTimeList);
    }

    @Override
    public long querySpeedBeatNumber(String examId, Long totalTime, String memberId, ExamType examType) {

        List<ExamResultDO> resultDOList = querySeniority(examId, ExamType.SPEED);
        Integer beatNumber = 0;
        for (ExamResultDO examResultDO : resultDOList) {
            if (!examResultDO.getUser().getMemberId().equals(memberId) && examResultDO.getTotalTime() > totalTime) {
                beatNumber++;
            }
        }

        return beatNumber;
    }

    @Override
    public List<ExamResultDO> querySpeedSeniority(String examId, ExamType examType) {

        return querySeniority(examId, examType);
    }

    @Override
    public Integer queryBestScore(String examId, String memberId) {
        List<ExamResultDO> userAllResult = examResultRepository.queryExamResultList(examId, memberId);

        List<Integer> userTotalScore = userAllResult.stream()
                .map(examResultDO -> {
                    if (examResultDO.getTotalScore() == null) {
                        examResultDO.setTotalScore(0);
                    }
                    return examResultDO.getTotalScore();
                })
                .collect(Collectors.toList());

        return Collections.max(userTotalScore);
    }

    @Override
    public long queryScoreBeatNumber(String examId, Integer totalScore, String memberId, ExamType examType) {

        List<ExamResultDO> resultDOList = querySeniority(examId, examType);
        Integer beatNumber = 0;
        for (ExamResultDO examResultDO : resultDOList) {
            if (!examResultDO.getUser().getMemberId().equals(memberId) && examResultDO.getTotalScore() < totalScore) {
                beatNumber++;
            }

        }

        return beatNumber;
    }

    @Override
    public List<ExamResultDO> queryScoreSeniority(String examId, ExamType examType) {

        return querySeniority(examId, examType);
    }

    /**
     * 获取所有排行榜列表
     *
     * @param examId   考试id
     * @param examType 考试类型
     * @return 所有用户排行
     */
    private List<ExamResultDO> querySeniority(String examId, ExamType examType) {
        List<ExamResultDO> allResult = examResultRepository.queryAllResult(examId);

        List<ExamResultDO> resultList = new LinkedList<>();
        List<String> judgeId = new ArrayList<>();

        //获取所有作答者最佳成绩的列表
        for (ExamResultDO examResultDO : allResult) {
            String id = examResultDO.getUser().getMemberId();
            ExamResultDO examResult = new ExamResultDO();
            if (!judgeId.contains(id)) {
                if (examResultDO.getTotalScore() == null) {
                    examResultDO.setTotalScore(0);
                }
                BeanUtils.copyProperties(examResultDO, examResult);
            }
            for (ExamResultDO resultDO : allResult) {
                if (id.equals(resultDO.getUser().getMemberId()) && !judgeId.contains(id)) {
                    if (resultDO.getTotalScore() == null) {
                        resultDO.setTotalScore(0);
                    }
                    if (examType == ExamType.SPEED && examResult.getTotalTime() > resultDO.getTotalTime()) {
                        BeanUtils.copyProperties(resultDO, examResult);
                    }
                    if (examType == ExamType.SCORE && examResult.getTotalScore() < resultDO.getTotalScore()) {
                        BeanUtils.copyProperties(resultDO, examResult);
                    }
                }
            }
            if (!judgeId.contains(id)) {
                resultList.add(examResult);
            }
            judgeId.add(id);

        }

        if (examType == ExamType.SPEED) {
            resultList.sort(Comparator.comparingLong(ExamResultDO::getTotalTime));
        }
        if (examType == ExamType.SCORE) {
            resultList.sort(Comparator.comparingInt(ExamResultDO::getTotalScore).reversed());
        }

        return resultList;
    }

}







