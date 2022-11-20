package com.wangyang.repository;

import com.wangyang.pojo.entity.Menu;
import com.wangyang.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends BaseRepository<Menu,Integer> {

    Menu findByCategoryId(int id);
    Menu findBySheetId(int id);
}
