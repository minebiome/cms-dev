package com.wangyang.weixin.service.impl;


import com.wangyang.weixin.entity.WxQrCode;
import com.wangyang.common.enums.CrudType;
import com.wangyang.weixin.pojo.WxQrCodeParam;
import com.wangyang.common.pojo.BaseVo;
import com.wangyang.weixin.repository.WxQrCodeRepository;
import com.wangyang.weixin.service.IWxQrCodeService;
import com.wangyang.common.service.AbstractCrudService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WxQrCodeServiceImpl extends AbstractCrudService<WxQrCode,WxQrCode, BaseVo,Integer> implements IWxQrCodeService {

    WxQrCodeRepository wxQrCodeRepository;
    private final WxMpService wxService;
    public WxQrCodeServiceImpl(WxQrCodeRepository wxQrCodeRepository,WxMpService wxService) {
        super(wxQrCodeRepository);
        this.wxQrCodeRepository = wxQrCodeRepository;
        this.wxService = wxService;
    }


    @Override
    public WxMpQrCodeTicket createQrCode(WxQrCodeParam form) {
        WxMpQrCodeTicket ticket;
        try {
            if (form.getIsTemp()) {//创建临时二维码
                ticket = wxService.getQrcodeService().qrCodeCreateTmpTicket(form.getSceneStr(), form.getExpireSeconds());
            } else {//创建永久二维码
                ticket = wxService.getQrcodeService().qrCodeCreateLastTicket(form.getSceneStr());
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        WxQrCode wxQrCode = new WxQrCode();
        wxQrCode.setTicket(ticket.getTicket());
        wxQrCode.setUrl(ticket.getUrl());
        wxQrCode.setIsTemp(form.getIsTemp());
        wxQrCode.setSceneStr(form.getSceneStr());

        if (form.getIsTemp()) {
            wxQrCode.setExpireTime(new Date(System.currentTimeMillis() + ticket.getExpireSeconds() * 1000L));
        }
        this.save(wxQrCode);
        return ticket;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
