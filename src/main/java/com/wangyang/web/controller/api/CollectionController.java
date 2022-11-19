package com.wangyang.web.controller.api;

import com.gimranov.libzotero.HttpHeaders;
import com.gimranov.libzotero.LibraryType;
import com.gimranov.libzotero.ZoteroService;
import com.gimranov.libzotero.model.Item;
import com.gimranov.libzotero.model.ObjectVersions;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.vo.CollectionVO;
import com.wangyang.service.ICollectionService;
import com.wangyang.service.ILiteratureService;
import com.wangyang.service.IZoteroService;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    @Autowired
    ICollectionService collectionService;

    @Autowired
    IZoteroService zoteroService;

    @GetMapping
    public List<CollectionVO> list(){
      return collectionService.listTree();
    }

    @GetMapping("/import")
    @Async
    public BaseResponse importData()  {
        zoteroService.importCollection();
        return BaseResponse.ok("");
    }


}
