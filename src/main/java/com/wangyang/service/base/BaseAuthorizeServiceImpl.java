package com.wangyang.service.base;


import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.repository.base.BaseAuthorizeRepository;

public class BaseAuthorizeServiceImpl<AUTHORIZE extends BaseAuthorize>  extends AbstractCrudService<AUTHORIZE,Integer>
        implements IBaseAuthorizeService<AUTHORIZE> {

    private final BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService;
    public BaseAuthorizeServiceImpl(BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService) {
        super(baseAuthorizeService);
        this.baseAuthorizeService =baseAuthorizeService;
    }
}
