package com.wangyang.service;

import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.ComponentsCategory;
import com.wangyang.pojo.enums.Lang;
import com.wangyang.pojo.params.ComponentsParam;
import com.wangyang.pojo.vo.BaseVo;
import com.wangyang.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IComponentsService  extends ICrudService<Components, Components, BaseVo,Integer> {

    Page<Components> list(Pageable pageable,Lang lang);

    List<Components> listNeedArticle();

    /**
     * 查找所有组件
     * @return
     */
    List<Components> listAll();

    List<Components> listAll(Lang lang);

    Components add(ComponentsParam componentsParam);


    List<Components> addAll(List<Components> templatePages);
    Components update(int id, ComponentsParam templatePageParam);

    Components findById(int id);

    Components findDetailsById(int id);

    Components delete(int id);
    void deleteAll();


    Map<String ,Object> getModel(Components components);

    Components findByDataName(String dataName);

    Components findByViewName(String path, String viewName);

    Components findByViewName(String viewName);
    Components findByEnName(String viewName);

    Components saveUpdate(Integer id, ComponentsParam componentsParam);
}
