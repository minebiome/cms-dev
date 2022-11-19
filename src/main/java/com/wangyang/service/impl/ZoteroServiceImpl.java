package com.wangyang.service.impl;

import com.gimranov.libzotero.HttpHeaders;
import com.gimranov.libzotero.LibraryType;
import com.gimranov.libzotero.SearchQuery;
import com.gimranov.libzotero.ZoteroService;
import com.gimranov.libzotero.model.Item;
import com.gimranov.libzotero.model.ObjectVersions;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.service.ICollectionService;
import com.wangyang.service.ILiteratureService;
import com.wangyang.service.IZoteroService;
import com.wangyang.util.AuthorizationUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ZoteroServiceImpl implements IZoteroService {

    @Autowired
    ILiteratureService literatureService;

    @Autowired
    ICollectionService collectionService;

    @Async
    @Override
    public void importLiterature(Integer userId)  {

        try {
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

            SearchQuery searchQueryVersion = new SearchQuery();
            searchQueryVersion.put("itemType","journalArticle");
            Call<ObjectVersions> itemVersions = zoteroService.getItemVersions(LibraryType.USER, Long.valueOf("8927145"), searchQueryVersion,null);
            ObjectVersions objectVersions = itemVersions.execute().body();
            int size = objectVersions.size();
            int num;
            if(size%100==0){
                num = size/100;
            }else {
                num =Integer.valueOf(size/100)+1;
            }
            List<Item> allItem = new ArrayList<>();
            for (int i=0;i<num;i++){
                SearchQuery searchQuery = new SearchQuery();
                searchQuery.put("limit",100);
                searchQuery.put("start",i*100);

                searchQuery.put("itemType","journalArticle");
                Call<List<Item>> items = zoteroService.getItems(LibraryType.USER, Long.valueOf("8927145"), searchQuery,null);
                List<Item> itemList = items.execute().body();
                allItem.addAll(itemList);
            }

            List<Collection> collections = collectionService.listAll();
            Map<String, Collection> collectionMap = ServiceUtil.convertToMap(collections, Collection::getKey);

            List<Literature> literatureList = new ArrayList<>();
            for (int i=0;i<allItem.size();i++){
                int id = i+1;
                Item item = allItem.get(i);
                Literature literature = new Literature();
                List<String> collectionNames = item.getData().getCollections();
                if(collectionNames.size()>0){
                    String name = collectionNames.get(0);
                    if(collectionMap.containsKey(name)){
                        Collection collection = collectionMap.get(name);
                        literature.setCategoryId(collection.getId());
                    }else {
                        literature.setCategoryId(-1);
                    }
                }else {
                    literature.setCategoryId(-1);
                }
                literature.setTitle(item.getData().getTitle());
                literature.setKey(item.getKey());
                literature.setUserId(userId);
                literature.setOriginalContent(item.getData().getAbstractNote());
                literatureList.add(literature);

            }

//        Map<String, Literature> collectionMap = ServiceUtil.convertToMap(collections, Collection::getKey);
//        for (Collection collection : collections){
//            if(collectionMap.containsKey(collection.getParentKey())){
//                Integer id = collectionMap.get(collection.getParentKey()).getId();
//                collection.setParentId(id);
//            }
//        }

            literatureService.deleteAll();
            literatureService.saveAll(literatureList);
//
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Async
    @Override
    public void importCollection()  {

        try {
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


            Call<List<com.gimranov.libzotero.model.Collection>> zoteroCollections = zoteroService.getCollections(LibraryType.USER, Long.valueOf("8927145"), null);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
