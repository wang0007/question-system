package com.neuq.question.web.rest.pojo;

import com.neuq.question.data.pojo.InAPIUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * @author yegk7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Setter
public class NotSignInResult extends NotSignInUserResult {

    public InAPIUser inApiUser;
}
