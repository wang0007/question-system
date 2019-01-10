package com.neuq.question.web.rest.management.answer.exam;


import com.neuq.question.data.dao.ExamResultRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ExamResultDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.ExamType;
import com.neuq.question.service.ExamResultService;
import com.neuq.question.web.rest.pojo.ScoreExamResult;
import com.neuq.question.web.rest.pojo.SpeedExamResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yegk7
 */
@RestController
@RequestMapping(value = "rest/v1/answer/exam/result/{examId}")
@RequiredArgsConstructor
@Api(value = "考试结果接口", description = "考试结果接口")
public class ExamResultController {

    private final UserRepsitory userRepsitory;

    private final ExamResultRepository examResultRepository;

    private final ExamResultService examResultService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "通过考试id，用户信息,考试场次查询本场考试结果", notes = "通过考试id，用户信息,考试场次查询本场考试结果")
    public ExamResultDO queryExamResult(@PathVariable(value = "examId") String examId,
                                        @RequestParam("memberId") String memberId,
                                        @RequestParam(value = "sequence") String sequence) {

        InAPIUser user = userRepsitory.queryById(memberId);

        return examResultRepository.queryExamResult(examId, user.getMemberId(), sequence);
    }

    @GetMapping(value = "score/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "得分模式，获取当前用户最好成绩和击败人数", notes = "得分模式，获取当前用户最好成绩和击败人数")
    public ScoreExamResult queryScoreExamBeatResult(@PathVariable(value = "examId") String examId,
                                                    @RequestParam("memberId") String memberId,
                                                    @RequestParam(value = "sequence") String sequence) {

        InAPIUser user = userRepsitory.queryById(memberId);

        ScoreExamResult scoreExamResult = new ScoreExamResult();

        Integer bestScore = examResultService.queryBestScore(examId, user.getMemberId());

        ExamResultDO examResultDO = examResultRepository.queryExamResult(examId, user.getMemberId(), sequence);

        long beatNumber = examResultService.queryScoreBeatNumber(examId, examResultDO.getTotalScore(), user.getMemberId(), ExamType.SCORE);

        scoreExamResult.setExamId(examId);
        scoreExamResult.setUser(user);
        scoreExamResult.setBestScore(bestScore);
        scoreExamResult.setBeatNumber(beatNumber);

        return scoreExamResult;

    }

    @GetMapping(value = "score/seniority", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "得分模式，获取本次考试所有参与人员排行榜", notes = "得分模式，获取本次考试所有参与人员排行榜")
    public List<ExamResultDO> queryScoreSeniority(@PathVariable(value = "examId") String examId) {

        return examResultService.queryScoreSeniority(examId, ExamType.SCORE);

    }

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取当前用户本场考试所有考试成绩列表", notes = "获取当前用户本场考试所有考试成绩列表")
    public List<ExamResultDO> queryExamResultList(@PathVariable(value = "examId") String examId,
                                                  @RequestParam("memberId") String memberId) {

        InAPIUser user = userRepsitory.queryById(memberId);

        return examResultRepository.queryExamResultList(examId, user.getMemberId());

    }

    @GetMapping(value = "speed/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "竞速模式，获取当前用户最好成绩和击败人数", notes = "竞速模式，获取当前用户最好成绩和击败人数")
    public SpeedExamResult queryExamBeatResult(@PathVariable(value = "examId") String examId,
                                               @RequestParam("memberId") String memberId,
                                               @RequestParam(value = "sequence") String sequence) {

        InAPIUser user = userRepsitory.queryById(memberId);

        SpeedExamResult speedExamResult = new SpeedExamResult();

        Long minTime = examResultService.queryUserMinTime(examId, user.getMemberId());

        ExamResultDO examResultDO = examResultRepository.queryExamResult(examId, user.getMemberId(), sequence);

        long beatNumber = examResultService.querySpeedBeatNumber(examId, examResultDO.getTotalTime(), user.getMemberId(), ExamType.SPEED);

        speedExamResult.setExamId(examId);
        speedExamResult.setUser(user);
        speedExamResult.setMinTime(minTime);
        speedExamResult.setBeatNumber(beatNumber);

        return speedExamResult;

    }

    @GetMapping(value = "speed/seniority", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "竞速模式，获取本次考试所有参与人员排行榜", notes = "竞速模式，获取本次考试所有参与人员排行榜")
    public List<ExamResultDO> querySeniority(@PathVariable(value = "examId") String examId) {

        return examResultService.querySpeedSeniority(examId, ExamType.SPEED);

    }


}
