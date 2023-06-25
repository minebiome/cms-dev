package com.wangyang.weixin.service;

import com.wangyang.weixin.entity.WxQrCode;
import com.wangyang.weixin.pojo.WxQrCodeParam;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.service.base.ICrudService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public interface IWxQrCodeService extends ICrudService<WxQrCode, WxQrCode, BaseVo,Integer> {
    WxMpQrCodeTicket createQrCode(WxQrCodeParam form);
}
