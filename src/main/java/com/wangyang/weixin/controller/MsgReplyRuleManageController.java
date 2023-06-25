package com.wangyang.weixin.controller;

import com.wangyang.weixin.entity.MsgReplyRule;
import com.wangyang.weixin.service.IMsgReplyRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/msgReplyRule")
@Api(tags = {"自动回复规则-管理后台"})
public class MsgReplyRuleManageController {
    @Autowired
    private IMsgReplyRuleService msgReplyRuleService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 保存
     */
    @PostMapping
//    @RequiresPermissions("wx:msgreplyrule:save")
    @ApiOperation(value = "保存")
    public MsgReplyRule save(@RequestBody MsgReplyRule msgReplyRule) {
        MsgReplyRule replyRule = msgReplyRuleService.save(msgReplyRule);

        return replyRule;
    }


    @GetMapping
    @ApiOperation(value = "列表")
    public Page<MsgReplyRule> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
//        params.put("appid",appid);
        Page<MsgReplyRule> codes = msgReplyRuleService.pageBy(pageable);
        return codes;
    }

    @PostMapping("/update/{id}")
    public MsgReplyRule update(@PathVariable("id") Integer id, @RequestBody MsgReplyRule msgReplyRule){
        return msgReplyRuleService.update(id,msgReplyRule);
    }
    @GetMapping("/delById/{id}")
    public MsgReplyRule delById(@PathVariable("id") Integer id){
        MsgReplyRule msgReplyRule = msgReplyRuleService.delBy(id);
        return msgReplyRule;
    }
}
