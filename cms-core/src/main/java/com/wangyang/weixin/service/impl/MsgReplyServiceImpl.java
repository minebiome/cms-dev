package com.wangyang.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangyang.weixin.entity.MsgReplyRule;
import com.wangyang.weixin.service.IMsgReplyRuleService;
import com.wangyang.weixin.service.IMsgReplyService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MsgReplyServiceImpl implements IMsgReplyService {
//    private static class ExcutorHolder{
//        /**
//         * 线程池
//         * corePoolSize=5 核心线程数
//         * maximumPoolSize=30 最大线程数
//         * keepAliveTime=10，unit=TimeUnit.SECOND 线程最大空闲时间为10秒
//         * workQueue=new SynchronousQueue<Runnable>() 链表队列
//         * handler=new ThreadPoolExecutor.CallerRunsPolicy()
//         */
//        private static final ExecutorService EXCUTOR = new ThreadPoolExecutor(
//                5,30,60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>(),
//                new ThreadPoolExecutor.CallerRunsPolicy());
//    }
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    IMsgReplyRuleService msgReplyRuleService;

    @Override
    public boolean tryAutoReply(boolean exactMatch, String toUser, String keywords) {
        try {
            List<MsgReplyRule> rules = msgReplyRuleService.getMatchedRules(exactMatch, keywords);
            if (rules.isEmpty()) {
                return false;
            }
//            long delay = 0;
            for (MsgReplyRule rule : rules) {
                taskExecutor.submit(() -> {
//                    wxMpService.switchover(appid);
                    this.reply(toUser,rule.getReplyType(),rule.getReplyContent());
                });

//                TaskExcutor.schedule(() -> {
////                    wxMpService.switchover(appid);
//                    this.reply(toUser,rule.getReplyType(),rule.getReplyContent());
//                }, delay, TimeUnit.MILLISECONDS);
//                delay += autoReplyInterval;
            }
            return true;
        } catch (Exception e) {
            log.error("自动回复出错：", e);
            e.printStackTrace();
        }
        return false;
    }

    private WxMpXmlOutNewsMessage replyLink(String toUser, String jsonParam) {
        JSONObject json = JSON.parseObject(jsonParam);
//        String signUrl = weiYaConfig.getSignInUrl();
//        String url = MessageFormat.format("https://w66h994817.zicp.fun/wx/index.html", wxMessage.getFromUser());
////        log.info("签到url:{}", "https://w66h994817.zicp.fun/wx/index.html");
//        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
//        item.setDescription("点击报告系统");
//        item.setPicUrl("https://mmbiz.qpic.cn/mmbiz_jpg/B0md6NdhhMRguia0l7AUGZ1mRUzm3ibv9fVqiblSON5VyS6ceAjWLZHGJQ9CnbeUKOOg1xkvQQB4QprfdkLmA9gicw/0?wx_fmt=jpeg");
//        item.setTitle("报告系统");
//        item.setUrl(url);
//
//        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
//                .fromUser(wxMessage.getToUser())
//                .toUser(wxMessage.getFromUser())
//                .addArticle(item)
//                .build();
        return null;
    }
    @Override
    public void replyText(String toUser, String content) throws WxErrorException {
        wxMpService.getKefuService().sendKefuMessage(WxMpKefuMessage.TEXT().toUser(toUser).content(content).build());
//        JSONObject json = new JSONObject().fluentPut("content",content);
//        wxMsgService.addWxMsg(WxMsg.buildOutMsg(WxConsts.KefuMsgType.TEXT,toUser,json));
    }

    @Override
    public void replyImage(String toUser, String mediaId) throws WxErrorException {

    }

    @Override
    public void replyVoice(String toUser, String mediaId) throws WxErrorException {

    }

    @Override
    public void replyVideo(String toUser, String mediaId) throws WxErrorException {

    }

    @Override
    public void replyMusic(String toUser, String mediaId) throws WxErrorException {

    }

    @Override
    public void replyNews(String toUser, String newsInfoJson) throws WxErrorException {
        WxMpKefuMessage.WxArticle wxArticle = JSON.parseObject(newsInfoJson, WxMpKefuMessage.WxArticle.class);
        List<WxMpKefuMessage.WxArticle> newsList = new ArrayList<WxMpKefuMessage.WxArticle>(){{add(wxArticle);}};
        wxMpService.getKefuService().sendKefuMessage(WxMpKefuMessage.NEWS().toUser(toUser).articles(newsList).build());

//        wxMsgService.addWxMsg(WxMsg.buildOutMsg(WxConsts.KefuMsgType.NEWS,toUser,JSON.parseObject(newsInfoJson)));
    }

    @Override
    public void replyMpNews(String toUser, String mediaId) throws WxErrorException {

    }

    @Override
    public void replyWxCard(String toUser, String cardId) throws WxErrorException {

    }

    @Override
    public void replyMiniProgram(String toUser, String miniProgramInfoJson) throws WxErrorException {

    }

    @Override
    public void replyMsgMenu(String toUser, String msgMenusJson) throws WxErrorException {

    }
}
