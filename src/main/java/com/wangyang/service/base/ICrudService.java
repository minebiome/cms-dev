package com.wangyang.service.base;

import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Collection;
import com.wangyang.pojo.entity.Comment;
import com.wangyang.pojo.enums.CrudType;
import com.wangyang.pojo.vo.CollectionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface ICrudService<DOMAIN,DOMAINDTO,DOMAINVO,ID> {
    List<DOMAIN> listAll();
    DOMAIN add(@NonNull DOMAIN domain);
    DOMAIN save(@NonNull DOMAIN domain);

    @Transactional
    void truncateTable();

    DOMAIN findById(@NonNull ID id);

    void deleteAll();


    void delete(DOMAIN t);

    Page<DOMAIN> pageBy(Pageable pageable);
    Page<DOMAIN> pageBy(Pageable pageable,String keywords);

    void deleteAll(Iterable<DOMAIN> domains);

    void createTSVFile(HttpServletResponse response);

    List<DOMAIN> saveAll(Iterable<DOMAIN> domain);

    File createTSVFile(List<DOMAIN> domains, String filePath, String[] heads);

    List<DOMAIN> tsvToBean(String filePath);
    DOMAIN delBy(ID id);
    List<DOMAIN> initData(String filePath,Boolean isEmpty);
    boolean supportType(@Nullable CrudType type);

    List<DOMAINVO> listWithTree(List<DOMAINVO> list);

    void updateOrder(List<DOMAIN> domains, List<DOMAINVO> domainvos);

    void updateOrder(List<DOMAINVO> domainvos);
    List<DOMAIN> listByIds(Set<ID> ids);

    List<DOMAIN>  findByParentId(Integer parentId);

    List<DOMAINVO> convertToListVo(List<DOMAIN> domains);
    //    @Override
}
