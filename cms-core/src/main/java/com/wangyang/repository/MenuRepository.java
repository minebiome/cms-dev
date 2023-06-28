package com.wangyang.repository;

import com.wangyang.pojo.entity.Menu;
import com.wangyang.common.repository.BaseRepository;

public interface MenuRepository extends BaseRepository<Menu,Integer> {

    Menu findByCategoryId(int id);
    Menu findBySheetId(int id);
}
