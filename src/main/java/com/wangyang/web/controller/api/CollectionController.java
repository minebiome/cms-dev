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


    @GetMapping
    public List<CollectionVO> list(){
      return collectionService.listTree();
    }

    @GetMapping("/import")
    @Async
    public BaseResponse importData() throws IOException {
//        ZoteroAuth zoteroAuth =  new ZoteroAPIKey("anXNFXA8ng0ri04DIAz99Vdd");
//        Library library = Library.createLibrary("8927145", zoteroAuth);
//
//        CollectionIterator collectionIterator = library.fetchCollectionsAll();
//        List<Collection> collections = new ArrayList<>();
//        while (collectionIterator.hasNext()){
//            zotero.api.Collection zoteroCollection = collectionIterator.next();
//            String key = zoteroCollection.getKey();
//            String name = zoteroCollection.getName();
//            Collection collection = new Collection();
//            collection.setName(name);
//            collection.setKey(key);
//            collections.add(collection);
//        }
//        collectionService.saveAll(collections);

//        RequestInterceptor interceptor = new RequestInterceptor() {
//            @Override
//            public void intercept(RequestFacade requestFacade) {
//                requestFacade.addHeader(HttpHeaders.AUTHORIZATION,HttpHeaders.AUTHORIZATION_BEARER_X + "anXNFXA8ng0ri04DIAz99Vdd");
//                requestFacade.addHeader(HttpHeaders.ZOTERO_API_VERSION,"3");
//            }
//        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header(HttpHeaders.AUTHORIZATION,HttpHeaders.AUTHORIZATION_BEARER_X + "anXNFXA8ng0ri04DIAz99Vdd")
                                .header(HttpHeaders.ZOTERO_API_VERSION,"3")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })

                .build();
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl("https://api.zotero.org")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ZoteroService zoteroService = retrofit.create(ZoteroService.class);
//        Map map = new HashMap<>();
//        retrofit2.Call<Map<String, String>> collectionsVersion = zoteroService.getCollectionsVersion(LibraryType.USER, Long.valueOf("8927145"), null);
//        Map<String, String> stringMap = collectionsVersion.execute().body();


        retrofit2.Call<List<com.gimranov.libzotero.model.Collection>> zoteroCollections = zoteroService.getCollections(LibraryType.USER, Long.valueOf("8927145"), null);
        List<com.gimranov.libzotero.model.Collection> zoteroCollection = zoteroCollections.execute().body();
        List<Collection> collections = new ArrayList<>();
        for (int i=0;i<zoteroCollection.size();i++){
            int id = i+1;
            com.gimranov.libzotero.model.Collection item = zoteroCollection.get(i);
            Collection collection = new Collection();
            collection.setId(id);
            collection.setKey(item.getKey());
            collection.setName(item.getData().getName());
            collection.setVersion(item.getVersion());
            collection.setParentKey(item.getData().getParentCollection());
            collections.add(collection);

        }

        Map<String, Collection> collectionMap = ServiceUtil.convertToMap(collections, Collection::getKey);
        for (Collection collection : collections){
            if(collectionMap.containsKey(collection.getParentKey())){
                Integer id = collectionMap.get(collection.getParentKey()).getId();
                collection.setParentId(id);
            }
        }

        collectionService.deleteAll();
        collectionService.saveAll(collections);
//
        return BaseResponse.ok("");
    }


}
