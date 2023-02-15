package com.wangyang.web.controller.api;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.service.IComponentsService;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.params.ComponentsParam;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/templatePage")
public class ComponentsController {

    @Autowired
    IComponentsService componentsService;

    @GetMapping
    public Page<Components> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return  componentsService.list(pageable);
    }

    @RequestMapping("/find/{id}")
    public Components findById(@PathVariable("id") Integer id){
        return componentsService.findById(id);
    }

    @PostMapping
    public Components add(@RequestBody  ComponentsParam componentsParam){
        return componentsService.add(componentsParam);
    }



    @PostMapping("/update/{id}")
    public Components update(@PathVariable("id") Integer id,@RequestBody  ComponentsParam componentsParam){

        return componentsService.update(id,componentsParam);
    }





    @RequestMapping("/delete/{id}")
    public Components delete(@PathVariable("id") Integer id){
        Components components = componentsService.delete(id);
        TemplateUtil.deleteTemplateHtml(components.getViewName(),components.getPath());
        return components;
    }


    @GetMapping("/generate/{id}")
    public BaseResponse generateHtml(@PathVariable("id") Integer id){
        Components components = componentsService.findById(id);
        Object o = componentsService.getModel(components);
        return BaseResponse.ok(TemplateUtil.convertHtmlAndSave(o, components));
    }

    @GetMapping("/fetchComponents")
    public List<Components> fetchComponents(@RequestParam(required = false) String path){
        String workDir = CMSUtils.getWorkDir();
        String componentsDir;
        if(path!=null && !path.equals("")){
            componentsDir=workDir+ File.separator+CMSUtils.getTemplates()+path;
        }else{
            componentsDir=workDir+ File.separator+CMSUtils.getTemplates()+"components";
        }
        List<String> fileNames = FileUtils.getFileNames(componentsDir);
        List<Components> components = componentsService.listAll();
        Set<String> templateValue = ServiceUtil.fetchProperty(components, Components::getTemplateValue);
        Set<String> filterFileNames = fileNames.stream().filter(item -> {
            return !templateValue.contains("components"+File.separator+item.replace(".html","")) && !item.endsWith("bak") && !item.contains(" ");
        }).collect(Collectors.toSet());

        if(filterFileNames.size()==0){
            throw new ObjectException("模板中没有新的文件！！");
        }

        List<Components> componentsList = new ArrayList<>();
        filterFileNames.forEach(item->{
            String name = item.replace("@", "").replace(".html", "").replace(" ","_");
            String viewPath = "components"+File.separator+item.replace(".html", "");
            componentsList.add( new Components(name, CMSUtils.getComponentsPath(), viewPath,name,CmsConst.ARTICLE_DATA,"",true,false));

        });

        List<Components> saveAll = componentsService.saveAll(componentsList);

        return saveAll;
    }



    @GetMapping("/listNeedArticle")
    public List<Components> listNeedArticle(){
        return componentsService.listNeedArticle();
    }


    @GetMapping("/findDetailsById/{id}")
    public Components findDetailsById(@PathVariable("id") Integer id){
        return componentsService.findDetailsById(id);
    }
}
