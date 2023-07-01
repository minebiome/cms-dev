package com.wangyang.weixin.service.impl;

import com.wangyang.weixin.service.ITemplateMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateMsgServiceImpl implements ITemplateMsgService {
    private final WxMpService wxService;
//    @Autowired
//    WxUserService wxUserService;



    /**
     * 发送微信模版消息,使用固定线程的线程池
     */
    @Override
    @Async
    public void sendTemplateMsg(WxMpTemplateMessage msg, String appid) {
        String result;
        try {
            wxService.switchover(appid);
            result = wxService.getTemplateMsgService().sendTemplateMsg(msg);
        } catch (WxErrorException e) {
            result = e.getMessage();
        }

        //保存发送日志
//        TemplateMsgLog log = new TemplateMsgLog(msg,appid, result);
//        templateMsgLogService.addLog(log);
    }

    @Override
    @Async
    public void sendMsgBatch(WxMpTemplateMessage form, List<String> openId) {
        log.info("批量发送模板消息任务开始,参数：{}",form.toString());
//        wxService.switchover(appid);
        WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
                .templateId(form.getTemplateId())
                .url(form.getUrl())
                .miniProgram(form.getMiniProgram())
                .data(form.getData());
        openId.forEach(item->{
            WxMpTemplateMessage msg = builder.toUser(item).build();
        });

//
//        Map<String, Object> filterParams = form.getWxUserFilterParams();
//        if(filterParams==null) {
//            filterParams=new HashMap<>(8);
//        }
//        long currentPage=1L,totalPages=Long.MAX_VALUE;
//        filterParams.put("appid",appid);
//        filterParams.put("limit","500");
//        while (currentPage<=totalPages){
//            filterParams.put("page",String.valueOf(currentPage));
//            //按条件查询用户
//            IPage<WxUser> wxUsers = wxUserService.queryPage(filterParams);
//            logger.info("批量发送模板消息任务,使用查询条件，处理第{}页，总用户数：{}",currentPage,wxUsers.getTotal());
//            wxUsers.getRecords().forEach(user->{
//
//                this.sendTemplateMsg(msg,appid);
//            });
//            currentPage=wxUsers.getCurrent()+1L;
//            totalPages=wxUsers.getPages();
//        }
        log.info("批量发送模板消息任务结束");
    }
}
