package com.wangyang.weixin.controller;

import com.wangyang.weixin.entity.WxQrCode;
import com.wangyang.weixin.pojo.WxQrCodeParam;
import com.wangyang.weixin.service.IWxQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/wxQrCode")
@Api(tags = {"公众号带参二维码-管理后台"})
public class WxQrCodeManageController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private IWxQrCodeService wxQrCodeService;


    /**
     * 创建带参二维码ticket
     */
    @PostMapping("/createTicket")
    @ApiOperation(value = "创建带参二维码ticket",notes = "ticket可以换取二维码图片")
    public WxMpQrCodeTicket createTicket( @RequestBody WxQrCodeParam form) throws WxErrorException {
//        wxMpService.switchoverTo(appid);
        WxMpQrCodeTicket ticket = wxQrCodeService.createQrCode(form);
        return ticket;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public Page<WxQrCode>  list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable) {
//        params.put("appid",appid);
        Page<WxQrCode> codes = wxQrCodeService.pageBy(pageable);

        return codes;
    }

//    @PostMapping("/update/{id}")
//    public Surveys update(@PathVariable("id") Integer id, @RequestBody WxQrCodeParam wxQrCodeParam){
//        return wxQrCodeService.update(id,wxQrCodeParam);
//    }
    @GetMapping("/delById/{id}")
    public WxQrCode delById(@PathVariable("id") Integer id){
        WxQrCode wxQrCode = wxQrCodeService.delBy(id);
        return wxQrCode;
    }


}
