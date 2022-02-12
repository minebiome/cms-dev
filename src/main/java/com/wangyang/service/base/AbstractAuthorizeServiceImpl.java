package com.wangyang.service.base;


import com.wangyang.pojo.authorize.BaseAuthorize;
import com.wangyang.repository.base.BaseAuthorizeRepository;

public abstract class AbstractAuthorizeServiceImpl<AUTHORIZE extends BaseAuthorize>  extends AbstractCrudService<AUTHORIZE,Integer>
        implements IAuthorizeService<AUTHORIZE> {

    private final BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService;
    public AbstractAuthorizeServiceImpl(BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService) {
        super(baseAuthorizeService);
        this.baseAuthorizeService =baseAuthorizeService;
    }
}
