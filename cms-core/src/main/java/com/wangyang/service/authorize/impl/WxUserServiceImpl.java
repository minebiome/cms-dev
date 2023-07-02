package com.wangyang.service.authorize.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.pojo.authorize.LoginUser;
import com.wangyang.pojo.authorize.WxUser;
import com.wangyang.common.enums.CrudType;
import com.wangyang.pojo.dto.WxUserToken;
import com.wangyang.pojo.support.Token;
import com.wangyang.repository.authorize.WxUserRepository;
import com.wangyang.service.authorize.IWxUserService;
import com.wangyang.service.base.AbstractAuthorizeServiceImpl;
import com.wangyang.util.TokenProvider;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class WxUserServiceImpl  extends AbstractAuthorizeServiceImpl<WxUser>
        implements IWxUserService {
    private final WxMpService wxService;
    private final WxUserRepository wxUserRepository;
    private final TokenProvider tokenProvider;
    private final WxMaService wxMaService;
    public WxUserServiceImpl(WxUserRepository wxUserRepository, WxMpService wxService,TokenProvider tokenProvider, WxMaService wxMaService){

        super(wxUserRepository);
        this.wxUserRepository=wxUserRepository;
        this.wxService =wxService;
        this.tokenProvider = tokenProvider;
        this.wxMaService = wxMaService;
    }


    @Override
    public WxUser loginNoSave(String code) {
        try {
            WxOAuth2AccessToken wxOAuth2AccessToken = wxService.getOAuth2Service().getAccessToken(code);
            String openid = wxOAuth2AccessToken.getOpenId();
//            WxUser wxUser = this.findBYOpenId(openid);
//            if(wxUser==null){
            WxUser wxUser = new WxUser();
            WxOAuth2UserInfo wxOAuth2User = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");
            wxUser.setOpenId(openid);
            wxUser.setAvatar(wxOAuth2User.getHeadImgUrl());
            wxUser.setNickname(wxOAuth2User.getNickname());
            wxUser.setGender(wxOAuth2User.getSex());
            wxUser.setRoleEn(CMSUtils.getWxRole());
//                wxUser = super.save(wxUser);
//            }
//            Token token = tokenProvider.generateTokenNoSave(wxUser);
//
//            LoginUser loginUser = new LoginUser();
//            loginUser.setToken(token.getToken());
//            loginUser.setExp(token.getExp());
////            loginUser.setId(wxUser.getId());
//            loginUser.setAvatar(wxUser.getAvatar());
//            loginUser.setGender(wxUser.getGender());
//            loginUser.setNickname(wxUser.getNickname());


            return wxUser;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WxUserToken loginWx(String code){
        try {
            WxOAuth2AccessToken wxOAuth2AccessToken = wxService.getOAuth2Service().getAccessToken(code);
            String openid = wxOAuth2AccessToken.getOpenId();
            WxUser wxUser = this.findBYOpenId(openid);
            if(wxUser==null){
                wxUser = new WxUser();
                WxOAuth2UserInfo wxOAuth2User = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");
                wxUser.setOpenId(openid);
                wxUser.setAvatar(wxOAuth2User.getHeadImgUrl());
                wxUser.setNickname(wxOAuth2User.getNickname());
                wxUser.setGender(wxOAuth2User.getSex());
                wxUser.setRoleEn(CMSUtils.getWxRole());
                wxUser = super.save(wxUser);
            }
            Token token = tokenProvider.generateToken(wxUser);


            WxUserToken wxUserToken = new WxUserToken();
            BeanUtils.copyProperties(wxUser,wxUserToken);
            wxUserToken.setToken(token);
            return wxUserToken;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LoginUser loginMp(String code){
        try {
            WxOAuth2AccessToken wxOAuth2AccessToken = wxService.getOAuth2Service().getAccessToken(code);
            String openid = wxOAuth2AccessToken.getOpenId();
            WxUser wxUser = this.findBYOpenId(openid);
            if(wxUser==null){
                wxUser = new WxUser();
                WxOAuth2UserInfo wxOAuth2User = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");
                wxUser.setOpenId(openid);
                wxUser.setAvatar(wxOAuth2User.getHeadImgUrl());
                wxUser.setNickname(wxOAuth2User.getNickname());
                wxUser.setGender(wxOAuth2User.getSex());
                wxUser.setRoleEn(CMSUtils.getWxRole());
                wxUser = super.save(wxUser);
            }
            Token token = tokenProvider.generateToken(wxUser);

            LoginUser loginUser = new LoginUser();
            loginUser.setToken(token.getToken());
            loginUser.setExp(token.getExp());
            loginUser.setId(wxUser.getId());
            loginUser.setAvatar(wxUser.getAvatar());
            loginUser.setGender(wxUser.getGender());
            loginUser.setNickname(wxUser.getNickname());


            return loginUser;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public LoginUser loginMa(String code) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();
            WxUser wxUser = this.findBYOpenId(openid);
            if(wxUser==null){
                wxUser = new WxUser();
//                WxOAuth2UserInfo wxOAuth2User = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");
                wxUser.setOpenId(openid);
//                wxUser.setAvatar(wxOAuth2User.getHeadImgUrl());
//                wxUser.setNickname(wxOAuth2User.getNickname());
//                wxUser.setGender(wxOAuth2User.getSex());
                wxUser.setRoleEn(CMSUtils.getWxRole());
                wxUser = super.save(wxUser);
            }
            Token token = tokenProvider.generateToken(wxUser);

            LoginUser loginUser = new LoginUser();
            loginUser.setToken(token.getToken());
            loginUser.setExp(token.getExp());
            loginUser.setId(wxUser.getId());
            loginUser.setSessionKey(sessionKey);
//            loginUser.setAvatar(wxUser.getAvatar());
//            loginUser.setGender(wxUser.getGender());
//            loginUser.setNickname(wxUser.getNickname());


            return loginUser;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public LoginUser login(WxUser inputWxUser){
        WxUser wxUser = this.findByPhoneId(inputWxUser.getOpenId());
        if(wxUser==null){
            wxUser = new WxUser();
            BeanUtils.copyProperties(inputWxUser, wxUser);
//                WxOAuth2UserInfo wxOAuth2User = wxService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, "zh_CN");
//                wxUser.setOpenId(openid);
//                wxUser.setAvatar(wxOAuth2User.getHeadImgUrl());
//                wxUser.setNickname(wxOAuth2User.getNickname());
//                wxUser.setGender(wxOAuth2User.getSex());
//                wxUser.setRoleEn(CMSUtils.getWxRole());
            wxUser.setRoleEn(CMSUtils.getPhoneRole());
            wxUser = super.save(wxUser);
        }
        Token token = tokenProvider.generateToken(wxUser);

        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token.getToken());
        loginUser.setExp(token.getExp());
        loginUser.setId(wxUser.getId());
        loginUser.setAvatar(wxUser.getAvatar());
        loginUser.setGender(wxUser.getGender());
        loginUser.setNickname(wxUser.getNickname());


        return loginUser;

    }



    @Override
    public WxUser findByPhoneId(String phone){
        List<WxUser> wxUsers = wxUserRepository.findAll(new Specification<WxUser>() {
            @Override
            public Predicate toPredicate(Root<WxUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(criteriaBuilder.equal(root.get("phone"),phone)).getRestriction();
            }
        });
        if(wxUsers.size()>0){
            return wxUsers.get(0);
        }
        return null;
    }


    public WxUser findBYOpenId(String openId){
        List<WxUser> wxUsers = wxUserRepository.findAll(new Specification<WxUser>() {
            @Override
            public Predicate toPredicate(Root<WxUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(criteriaBuilder.equal(root.get("openId"),openId)).getRestriction();
            }
        });
        if(wxUsers.size()>0){
            return wxUsers.get(0);
        }
        return null;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
