package com.neuq.question.web.rest.management.answer.exam;


import com.neuq.question.data.dao.QuestionResultRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.QuestionResultDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author yegk7
 */
@RestController
@RequestMapping(value = "rest/v1/answer/question/result/{examId}")
@RequiredArgsConstructor
@Api(value = "问题作答结果信息接口", description = "问题作答结果信息接口")
public class QuestionResultController {

    private final QuestionResultRepository questionResultRepository;

    private final UserRepsitory userRepsitory;

    @ApiOperation(value = "通过问题id,考试场次查询本次考试题目作答结果", notes = "通过问题id查询题目作答结果")
    @GetMapping(value = "/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionResultDO queryResultById(@PathVariable(value = "examId") String examId,
                                            @RequestParam("memberId") String memberId,
                                            @PathVariable(value = "questionId") String questionId,
                                            @RequestParam(value = "sequence") String sequence) {

        InAPIUser user = userRepsitory.queryById(memberId);

        return questionResultRepository.queryResultById(questionId, user.getMemberId(), sequence);

    }

}
