package com.wangyang.service.base;


import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.repository.base.AuthorizeRepository;

public abstract class AbstractAuthorizeServiceImpl<AUTHORIZE extends BaseAuthorize>  extends AbstractCrudService<AUTHORIZE,BaseAuthorize, BaseVo,Integer>
        implements IAuthorizeService<AUTHORIZE> {

    private final AuthorizeRepository<AUTHORIZE> baseAuthorizeService;
    public AbstractAuthorizeServiceImpl(AuthorizeRepository<AUTHORIZE> baseAuthorizeService) {
        super(baseAuthorizeService);
        this.baseAuthorizeService =baseAuthorizeService;
    }
}