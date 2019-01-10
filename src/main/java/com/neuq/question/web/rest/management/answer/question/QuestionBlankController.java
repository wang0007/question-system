package com.neuq.question.web.rest.management.answer.question;


import com.neuq.question.data.dao.QuestionBlankRepository;
import com.neuq.question.data.dao.QuestionRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.QuestionBlankDO;
import com.neuq.question.service.QuestionBlankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yegk7
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "rest/v1/question/blank")
@Api(value = "题库操作接口", description = "题库相关配置操作接口")
public class QuestionBlankController {

    private final QuestionBlankService blankService;

    private final QuestionRepository questionRepository;

    private final QuestionBlankRepository blankRepository;

    @ApiOperation(value = "题库查询显示", notes = "题库查询，分页，显示")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QuestionBlankVO> getList(@RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "size", defaultValue = "20") int size,
                                         @RequestParam(value = "enable", required = false) Boolean enable,
                                         @RequestParam(value = "keyword", required = false) String keyword) {

        List<QuestionBlankDO> questionBlankDOList = blankService.getList(start, size, enable, keyword);

        return questionBlankDOList.stream().map(questionBlankDO -> {

                    QuestionBlankVO questionBlankVO = new QuestionBlankVO();
                    questionBlankVO.setQuestionBlankDO(questionBlankDO);
                    questionBlankVO.setSingleCount(questionRepository.querySingleQuestionList(questionBlankDO.getId()).size());
                    questionBlankVO.setMultipleCount(questionRepository.queryMultipleQuestionList(questionBlankDO.getId()).size());

                    return questionBlankVO;
                }
        ).collect(Collectors.toList());

    }


    @ApiOperation(value = "新增题库", notes = "新增题库")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuestionBlankDO insert(@RequestBody QuestionBlankDTO questionBlankDTO) {

        QuestionBlankDO questionBlankDO = new QuestionBlankDO();
        questionBlankDO.setName(questionBlankDTO.getName());
        questionBlankDO.setDescription(questionBlankDTO.getDescription());
        blankRepository.save(questionBlankDO);
        return questionBlankDO;
    }

    @ApiOperation(value = "根据题库id查询题库信息", notes = "根据题库id查询题库信息")
    @GetMapping(value = "{blankId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionBlankVO getQuestionBlank(@PathVariable("blankId") String blankId) {

        QuestionBlankDO questionBlankDO = blankRepository.queryById(blankId);

        QuestionBlankVO questionBlankVO = new QuestionBlankVO();

        questionBlankVO.setQuestionBlankDO(questionBlankDO);

        questionBlankVO.setSingleCount(questionRepository.querySingleQuestionList(questionBlankDO.getId()).size());

        questionBlankVO.setMultipleCount(questionRepository.queryMultipleQuestionList(questionBlankDO.getId()).size());

        return questionBlankVO;
    }

    @ApiOperation(value = "更新题库", notes = "根据题库id更新题库")
    @PutMapping(value = "{blankId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QuestionBlankDO update(@RequestBody QuestionBlankDTO questionBlankDTO,
                                  @PathVariable("blankId") String blankId) {

        blankRepository.update(questionBlankDTO, blankId);

        return blankRepository.queryById(blankId);
    }

    @ApiOperation(value = "更改题库为可用状态", notes = "更改题库为可用状态")
    @PutMapping(value = "/{blankId}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void enable(@PathVariable("blankId") String blankId) {

        blankRepository.setEnable(blankId);

    }

    @ApiOperation(value = "更改题库为不可用状态", notes = "更改题库为不可用状态")
    @PutMapping(value = "/{blankId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void disable(@PathVariable("blankId") String blankId) {

        blankRepository.setDisable(blankId);

    }

    @Data
    public static class QuestionBlankDTO {

        private String name;

        private String description;
    }

    @Data
    private class QuestionBlankVO {

        QuestionBlankDO questionBlankDO;

        Integer singleCount;

        Integer multipleCount;

    }
}
