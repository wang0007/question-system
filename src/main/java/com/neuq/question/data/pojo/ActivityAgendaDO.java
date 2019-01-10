package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.AbstractActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author liuhaoi
 */
@Document(collection = "activity.agenda")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityAgendaDO extends AbstractActivity {

    public static final String FIELD_WITH_AM_PM_TITLE = "withAmPmTitle";
    public static final String FIELD_AGENDAS = "agendas";
    public static final String FIELD_AGENDAS_ID = "agendas.agendaId";
    /**
     * 是否区分上下午日程
     */
    private Boolean withAmPmTitle;

    private List<Agenda> agendas;

    @Data
    public static class Agenda {

        private String agendaId;

        private String title;

        private String subTitle;

        private String startTime;

        private String endTime;
    }


}
