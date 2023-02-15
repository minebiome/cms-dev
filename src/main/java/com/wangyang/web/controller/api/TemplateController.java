package com.wangyang.web.controller.api;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.service.IHtmlService;
import com.wangyang.service.ITemplateService;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.params.TemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/template")
public class TemplateController {
    @Autowired
    ITemplateService templateService;

    @Autowired
    IHtmlService htmlService;


    public List<String> listTemplateType(){
        return null;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @RequestPart("file")
    @Anonymous
    public Template upload(@RequestPart("file") MultipartFile file){
        Template template = templateService.addZipFile(file);
        return  template;
    }
    /**
     * 根据template类型获取Template
     * @param type
     * @return
     */
    @GetMapping("/find/{type}")
    public List<Template> findByType(@PathVariable("type") TemplateType type){

        return templateService.findByTemplateType(type);
    }

    @PostMapping("/update/{id}")
    public Template update(@PathVariable("id") Integer id, @RequestBody TemplateParam templateParam){
        return templateService.update(id,templateParam);
    }

    @GetMapping("/findDetailsById/{id}")
    public Template findDetailsById(@PathVariable("id") Integer id){
        return templateService.findDetailsById(id);
    }


    @GetMapping
    public Page<Template> list(@PageableDefault(sort = {"id"},direction = DESC)Pageable pageable){
        Page<Template> templatePage = templateService.list(pageable);

        return templatePage;
    }

    @GetMapping("/tree/{id}")
    public Template setStatus(@PathVariable("id") int id){
        Template template = templateService.tree(id);

//        Template template = templateService.setStatus(id);
//        htmlService.generateHome();
        return template;
    }

    @PostMapping
    public Template add(@RequestBody Template templateInput){
        Template template = templateService.findByEnNameReturnNUll(templateInput.getEnName());
        if(template!=null){
            throw new ObjectException(template.getEnName()+"已经存在!!");
        }
        template = templateService.add(templateInput);
        File file = new File(CMSUtils.getWorkDir()+File.separator+CMSUtils.getTemplates() +template.getTemplateValue()+".html");
        if(!file.exists()){
            FileUtils.saveFile(file,"空模板 for" + templateInput.getTemplateValue());
        }
        return template;
    }

    @GetMapping("/delete/{id}")
    public Template delete(@PathVariable("id") Integer id){

        return templateService.deleteById(id);
    }

//    public void deleteById(Integer id){
//        templateService.deleteById(id);
//    }
}
