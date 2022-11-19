package com.wangyang.service;

import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.base.Content;
import com.wangyang.pojo.params.CategoryQuery;
import com.wangyang.pojo.vo.CategoryVO;
import com.wangyang.service.base.IBaseCategoryService;
import com.wangyang.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ICategoryService  extends IBaseCategoryService<Category,Category,CategoryVO> {
    Category save(Category category);

    /**
     * add category
     * @param category
     * @return
     */
    Category create(Category category);
    /**
     * add category
     * @param category
     * @return
     */
    Category update(Category category);

//    Page<CategoryDto> pageBy(String categoryEnName, int page, int size);

    Page<Category> pageBy(String categoryEnName, Pageable pageable);

    List<CategoryDto> listBy(String categoryEnName);



    List<Category> findAllById(Iterable<Integer> ids);

    /**
     * delete by Id
     * @param id
     */
    Category deleteById(int id);



    /**
     * find category by id
     * @param id
     * @return
     */
    Category findById(int id);




    List<CategoryDto> listRecommend();

    Optional<Category> findOptionalById(int id);

    List<Category> list(CategoryQuery categoryQuery, Sort sort);

    List<CategoryDto> listAllDto();

    List<Category> listAll();

    /**
     * 不显示haveHtml=false的Category
     * 生成菜单树
     * @return
     */
    List<CategoryVO> listUserCategoryVo();

    /**
     * 显示haveHtml=false的Category
     * @return
     */
    List<CategoryVO> listAdminCategoryVo();

//    List<Category> list();

    Category recommendOrCancelHome(int id);

    Category haveHtml(int id);

    Category addOrRemoveToMenu(int id);



    Category findByViewName(String viewName);

    void updateOrder(List<CategoryVO> categoryVOList);
}
