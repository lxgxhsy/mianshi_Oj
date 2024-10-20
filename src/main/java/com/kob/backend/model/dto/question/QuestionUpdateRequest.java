package com.kob.backend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新题目请求 管理员
 *
 * @author sy
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 推荐答案
     */
    private String answer;


    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}