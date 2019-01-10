package com.neuq.question.service.impl;

import com.google.common.collect.Lists;
import com.neuq.question.data.dao.ConferenceGuestRepository;
import com.neuq.question.data.pojo.ConferenceGuestDO;
import com.neuq.question.domain.excal.domain.ExcelData;
import com.neuq.question.error.ECIllegalArgumentException;
import com.neuq.question.service.ConferenceGuestService;
import com.neuq.question.web.rest.pojo.ExcelImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liuhaoi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConferenceGuestServiceImpl implements ConferenceGuestService {

    private final ConferenceGuestRepository guestRepository;


    @Override
    public ExcelImportResult importFromExcel(byte[] bytes, String conferenceId) {


        Map<String, Integer> fieldNames = new HashMap<>(8);

        fieldNames.put("name", 0);
        fieldNames.put("mobile", 1);
        fieldNames.put("email", 2);
        fieldNames.put("department", 3);
        fieldNames.put("company", 4);
        fieldNames.put("position", 5);
        fieldNames.put("gender", 6);
        fieldNames.put("birthday", 7);

        ExcelData<ConferenceGuestDO> data = new ExcelData<>(bytes, Lists.newArrayList(0), 2,
                ConferenceGuestDO.class, fieldNames);

        ExcelImportResult result = new ExcelImportResult();

        result.setFailed(0);
        result.setSucceed(0);

        data.processData(row -> {
            try {
                ConferenceGuestDO guestDO = row.getData();
                verify(guestDO);
                guestRepository.upsert(guestDO, conferenceId);
                result.successOnce();
            } catch (Exception e) {
                row.setDataValid(false);
                result.failedOnce();
            }
        });

        if (result.getFailed() > 0) {

            byte[] excelResult = data.exportResult();

            result.setExcelData(excelResult);
        }

        return result;
    }

    private final Validator validator;

    private void verify(ConferenceGuestDO guest) {

        Set<ConstraintViolation<ConferenceGuestDO>> validate = validator.validate(guest);

        if (validate.isEmpty()) {
            return;
        }

        throw new ECIllegalArgumentException("record invalid with message " + validate);

    }


}
