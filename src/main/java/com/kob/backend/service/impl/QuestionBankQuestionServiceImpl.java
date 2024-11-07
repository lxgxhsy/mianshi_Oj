package com.kob.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.common.ErrorCode;
import com.kob.backend.constant.CommonConstant;
import com.kob.backend.exception.BusinessException;
import com.kob.backend.exception.ThrowUtils;
import com.kob.backend.mapper.QuestionBankQuestionMapper;
import com.kob.backend.model.dto.questionBankQuestion.QuestionBankQuestionQueryRequest;
import com.kob.backend.model.entity.Question;
import com.kob.backend.model.entity.QuestionBank;
import com.kob.backend.model.entity.QuestionBankQuestion;
import com.kob.backend.model.entity.User;
import com.kob.backend.model.vo.QuestionBankQuestionVO;
import com.kob.backend.model.vo.UserVO;
import com.kob.backend.service.QuestionBankQuestionService;
import com.kob.backend.service.QuestionBankService;
import com.kob.backend.service.QuestionService;
import com.kob.backend.service.UserService;
import com.kob.backend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题库题目关联服务实现
 *
 * @author sy
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements QuestionBankQuestionService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionService questionService;

    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add) {
        ThrowUtils.throwIf(questionBankQuestion == null, ErrorCode.PARAMS_ERROR);
        //题库和题目必须存在
        Long questionId = questionBankQuestion.getQuestionId();
        if(questionId != null){
            Question question = questionService.getById(questionId);
            ThrowUtils.throwIf(question == null,ErrorCode.PARAMS_ERROR, "题目不存在");
        }

        Long questionBankId = questionBankQuestion.getQuestionBankId();
        if(questionBankId != null){
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            ThrowUtils.throwIf(questionBank == null,ErrorCode.PARAMS_ERROR, "题库不存在");
        }

        // 创建数据时，参数不能为空
       /* if (add) {
            // todo 补充校验规则

        }*/
       /* // 修改数据时，有参数则校验
        // todo 补充校验规则
     *//*   if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }*/
    }

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        String sortField = questionBankQuestionQueryRequest.getSortField();
        String sortOrder = questionBankQuestionQueryRequest.getSortOrder();
        Long userId = questionBankQuestionQueryRequest.getUserId();
        Long questionBankId = questionBankQuestionQueryRequest.getQuestionBankId();
        Long questionId = questionBankQuestionQueryRequest.getQuestionId();

        // todo 补充需要的查询条件



        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "notId", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题库题目关联封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    @Override
    public QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankQuestionVO questionBankQuestionVO = QuestionBankQuestionVO.objToVo(questionBankQuestion);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBankQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankQuestionVO.setUser(userVO);


        return questionBankQuestionVO;
    }

    /**
     * 分页获取题库题目关联封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request) {
        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionPage.getRecords();
        Page<QuestionBankQuestionVO> questionBankQuestionVOPage = new Page<>(questionBankQuestionPage.getCurrent(), questionBankQuestionPage.getSize(), questionBankQuestionPage.getTotal());
        if (CollUtil.isEmpty(questionBankQuestionList)) {
            return questionBankQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankQuestionVO> questionBankQuestionVOList = questionBankQuestionList.stream().map(questionBankQuestion -> {
            return QuestionBankQuestionVO.objToVo(questionBankQuestion);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBankQuestionList.stream().map(QuestionBankQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        questionBankQuestionVOList.forEach(questionBankQuestionVO -> {
            Long userId = questionBankQuestionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankQuestionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionBankQuestionVOPage.setRecords(questionBankQuestionVOList);
        return questionBankQuestionVOPage;
    }


    /**
     *  批量添加题目到题库
     * @param questionIdList
     * @param questionBankId
     * @param loginUser
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, User loginUser) {
		//参数校验
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR,"题库信息非法");
        ThrowUtils.throwIf(loginUser == null,ErrorCode.NOT_LOGIN_ERROR);

        //检查题目id是否存在
        List<Question> questionList = questionService.listByIds(questionIdList);

        //合法的题目id
        List<Long> validQuestionIdList = questionList.stream()
                .map(Question::getId)
                .collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(validQuestionIdList), ErrorCode.PARAMS_ERROR, "题目列表为空");
        //检查那些题目还不存在于题库中，避免重复插入
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .notIn(QuestionBankQuestion::getQuestionId, validQuestionIdList);
        List<QuestionBankQuestion> NotValidQuestionList = this.list(lambdaQueryWrapper);
        //合法的题目id
         validQuestionIdList = NotValidQuestionList.stream()
                .map(QuestionBankQuestion::getId)
                .collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(validQuestionIdList),ErrorCode.PARAMS_ERROR,"所有题目都已存在于题库中");
        //检查题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null,ErrorCode.NOT_FOUND_ERROR,"题库不存在");
	    //分批次处理 避免长事务 一次处理1000条数据
        int batchSize = 1000;
        int totalQuestionListSize = validQuestionIdList.size();
        //执行插入
        for(int i = 0;i < totalQuestionListSize; i += batchSize){
            //生成每个批次的数据
            List<Long> subList = validQuestionIdList.subList(i,Math.min(i,Math.min(i + batchSize, totalQuestionListSize)));
            List<QuestionBankQuestion> questionBankQuestions = subList.stream()
                    .map(questionId -> {
                        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                        questionBankQuestion.setQuestionBankId(questionBankId);
                        questionBankQuestion.setQuestionId(questionId);
                        questionBankQuestion.setUserId(loginUser.getId());
                        return questionBankQuestion;
                    }).collect(Collectors.toList());

            // 使用事务处理每批数据 使用代理调用方法 1.从上下文拿bean 2.
            QuestionBankQuestionService questionBankQuestionService = (QuestionBankQuestionServiceImpl) AopContext.currentProxy();
            questionBankQuestionService.batchAddQuestionsToBankInner(questionBankQuestions);
        }
    }

    /**
     *  批量添加题目到题库(事务 只有在内部使用)
     * @param questionBankQuestions
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddQuestionsToBankInner(List<QuestionBankQuestion> questionBankQuestions){
	    for(QuestionBankQuestion questionBankQuestion : questionBankQuestions){
            Long questionBankId = questionBankQuestion.getQuestionBankId();
            Long questionId = questionBankQuestion.getQuestionId();
            try{
                boolean result = this.save(questionBankQuestion);
                if(!result){
                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"向题库添加题目失败");
                }
            }catch (DataIntegrityViolationException e) {
                log.error("数据库唯一键冲突或违反其他完整性约束，题目 id: {}, 题库 id: {}, 错误信息: {}",
                        questionId, questionBankId, e.getMessage());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已存在于该题库，无法重复添加");
            }catch (DataAccessException e){
                log.error("数据库连接问题，事务问题等导致操作失败，题目id:{},题库id:{},错误信息：{}",questionId,questionBankId,e.getMessage());
            }catch(Exception e){
                log.error("添加题目到题库时发生未知错误,题目id:{},题库id:{},错误信息：{}",questionId,questionBankId,e.getMessage());
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"向题库添加题目失败");
            }
        }
    }

    /**
     *  批量删除题库中的题目信息
     * @param questionIdList
     * @param questionBankId
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void batchRemoveQuestionsFromBank(List<Long> questionIdList, Long questionBankId) {
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR,"题库信息非法");

        for(Long questionId : questionIdList){
            LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .eq(QuestionBankQuestion::getQuestionId, questionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            boolean result = this.remove(lambdaQueryWrapper);
            ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR,"从题库删除题目失败");
        }
	}



}
