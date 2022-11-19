package com.wangyang.web.controller.api;

import com.gimranov.libzotero.*;
import com.gimranov.libzotero.model.Item;
import com.gimranov.libzotero.model.ObjectVersions;
import com.google.gson.Gson;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.service.*;
import com.wangyang.util.AuthorizationUtil;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
//import retrofit.RequestInterceptor;
//import retrofit.RestAdapter;
//import retrofit.converter.GsonConverter;
//import rx.Observable;
//import zotero.api.Collection;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/literature")
public class LiteratureController {
    @Autowired
    ILiteratureService literatureService;

    @Autowired
    ICollectionService collectionService;

    @Autowired
    ITemplateService templateService;

    @Autowired
    IComponentsService componentsService;

    @PostMapping
    public Literature add(@RequestBody Literature literature){
        Literature saveLiterature = literatureService.add(literature);
        return saveLiterature;
    }

    @GetMapping
    public Page<Literature> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return literatureService.pageBy(pageable);
    }

    @PostMapping("/update/{id}")
    public Literature update(@RequestBody  Literature literatureParam,@PathVariable("id") Integer id){
        Literature literature = literatureService.findById(id);
        BeanUtils.copyProperties(literatureParam,literature,"id");
        return literatureService.save(literature);
    }

    @GetMapping("/find/{id}")
    public Literature findById(@PathVariable("id") Integer id){
        return literatureService.findById(id);
    }

    @RequestMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id){
        Literature literature = literatureService.findById(id);
        literatureService.delete(literature);

        return BaseResponse.ok("Delete id "+id+" menu success!!");
    }

    @GetMapping("/import")
    @Async
    public BaseResponse importData(HttpServletRequest request) throws IOException {
        int userId = AuthorizationUtil.getUserId(request);
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
        return BaseResponse.ok("");
    }

    @GetMapping("/generateHtml")
    public BaseResponse generateHtml(HttpServletRequest request){
        int userId = AuthorizationUtil.getUserId(request);
        List<Literature> literatureList = literatureService.listAll();
        List<Collection> collections = collectionService.listAll();
        Template template = templateService.findByEnName(CmsConst.DEFAULT_LITERATURE_TEMPLATE);

        Components components = componentsService.findByViewName("collectionTree");
        Object o = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(o, components);
        for (Collection collection:collections){
            List<Literature> subLiterature = literatureList.stream().filter(literature ->
                    literature.getCategoryId().equals(collection.getId())
            ).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map = new HashMap<>();
            map.put("view",subLiterature);
            map.put("template",template);
            String html = TemplateUtil.convertHtmlAndSave(CMSUtils.getLiteraturePath(),collection.getKey(),map, template);
        }
        return BaseResponse.ok("success!!");

    }
}
