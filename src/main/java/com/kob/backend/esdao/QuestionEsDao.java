package com.kob.backend.esdao;

import com.kob.backend.model.dto.question.QuestionEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Author: shiyong
 * @CreateTime: 2024-11-03
 * @Description: 题目Es操作
 */
public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {

	/**
	 * 根据用户id es查询题目
	 * @param userId
	 * @return
	 */
	List<QuestionEsDao> findByUserId(Long userId);
}