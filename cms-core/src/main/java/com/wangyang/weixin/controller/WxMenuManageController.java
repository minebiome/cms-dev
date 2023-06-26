package com.wangyang.weixin.controller;

import com.wangyang.common.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wxMenu")
@RequiredArgsConstructor
public class WxMenuManageController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 获取公众号菜单
     */
    @GetMapping("/getMenu")
    @ApiOperation(value = "获取公众号菜单")
    public WxMpMenu getMenu() throws WxErrorException {
//        wxMpService.switchoverTo(appid);
        WxMpMenu wxMpMenu = wxService.getMenuService().menuGet();
        return wxMpMenu;
    }


    /**
     * 创建、更新菜单
     */
    @PostMapping("/updateMenu")
    @ApiOperation(value = "创建、更新菜单")
    public BaseResponse updateMenu(@RequestBody WxMenu wxMenu) throws WxErrorException {
//        wxMpService.switchoverTo(appid);
        wxService.getMenuService().menuCreate(wxMenu);
        return BaseResponse.ok("success!");
    }

}
