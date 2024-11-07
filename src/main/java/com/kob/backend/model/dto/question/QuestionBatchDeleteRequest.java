package com.kob.backend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建题目请求
 *
 * @author sy
 */
@Data
public class QuestionBatchDeleteRequest implements Serializable {

    /**
     * 题目 id
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}