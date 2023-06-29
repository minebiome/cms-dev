package com.wangyang.weixin.service.impl;

import com.wangyang.weixin.entity.MsgReplyRule;
import com.wangyang.common.enums.CrudType;
import com.wangyang.common.pojo.BaseVo;
import com.wangyang.weixin.repository.MsgReplyRuleRepository;
import com.wangyang.weixin.service.IMsgReplyRuleService;
import com.wangyang.common.service.AbstractCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MsgReplyRuleServiceImpl  extends AbstractCrudService<MsgReplyRule,MsgReplyRule, BaseVo,Integer> implements IMsgReplyRuleService {


    MsgReplyRuleRepository msgReplyRuleRepository;
    public MsgReplyRuleServiceImpl(MsgReplyRuleRepository msgReplyRuleRepository) {
        super(msgReplyRuleRepository);
        this.msgReplyRuleRepository = msgReplyRuleRepository;
    }

    @Override
    public List<MsgReplyRule> getMatchedRules(boolean exactMatch, String keywords) {
        LocalTime now = LocalTime.now();
        List<MsgReplyRule> replyRuleList = msgReplyRuleRepository.findAll(new Specification<MsgReplyRule>() {
            @Override
            public Predicate toPredicate(Root<MsgReplyRule> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(criteriaBuilder.equal(root.get("status"),true),
                        criteriaBuilder.isNotNull(root.get("matchValue"))
                        ).getRestriction();
            }
        }, Sort.by(Sort.Order.desc("priority")));


        return  replyRuleList.stream()
//                .filter(rule->!StringUtils.hasText(rule.getAppid()) || appid.equals(rule.getAppid())) // 检测是否是对应公众号的规则，如果appid为空则为通用规则
//                .filter(rule->null == rule.getEffectTimeStart() || rule.getEffectTimeStart().toLocalTime().isBefore(now))// 检测是否在有效时段，effectTimeStart为null则一直有效
//                .filter(rule->null == rule.getEffectTimeEnd() || rule.getEffectTimeEnd().toLocalTime().isAfter(now)) // 检测是否在有效时段，effectTimeEnd为null则一直有效
                .filter(rule->isMatch(exactMatch || rule.isExactMatch(),rule.getMatchValue().split(",") ,keywords)) //检测是否符合匹配规则
                .collect(Collectors.toList());
//        return replyRuleList;
    }
    /**
     * 检测文字是否匹配规则
     * 精确匹配时，需关键词与规则词语一致
     * 非精确匹配时，检测文字需包含任意一个规则词语
     *
     * @param exactMatch 是否精确匹配
     * @param ruleWords  规则列表
     * @param checkWords 需检测的文字
     * @return
     */
    public static boolean isMatch(boolean exactMatch, String[] ruleWords, String checkWords) {
        if (!StringUtils.hasText(checkWords)) {
            return false;
        }
        for (String words : ruleWords) {
            if (exactMatch && words.equals(checkWords)) {
                return true;//精确匹配，需关键词与规则词语一致
            }
            if (exactMatch && words.equals(checkWords.replace("qrscene_",""))) {
                return true;//精确匹配，需关键词与规则词语一致
            }
            if (!exactMatch && checkWords.contains(words)) {
                return true;//模糊匹配
            }
        }
        return false;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
