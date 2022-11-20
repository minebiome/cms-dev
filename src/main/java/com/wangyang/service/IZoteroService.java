package com.wangyang.service;

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
import com.wangyang.pojo.entity.Task;
import com.wangyang.util.AuthorizationUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IZoteroService {


    Task importLiterature(int userId);

    Task importCollection(int userId);

    @Async
    void importLiterature(Integer userId, Task task);

    @Async
    void importCollection(Integer userId, Task task);
}
