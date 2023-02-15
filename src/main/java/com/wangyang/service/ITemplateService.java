package com.wangyang.service;

import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.params.TemplateParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ITemplateService {
    Optional<Template> findOptionalById(int id);

    List<Template> saveAll(List<Template> templates);
    Template add(Template template);
    Template update(int id, TemplateParam templateParam);

    Template findDetailsById(int id);

    List<Template> findAll();

    Template deleteById(int id);
    Template findById(int id);
    Page<Template> list(Pageable pageable);

    List<Template> listByAndStatusTrue(TemplateType templateType);

    Template findByEnNameReturnNUll(String enName);

    Template findByEnName(String enName);

    Template findOptionalByEnName(String enName);

    void deleteAll();
    List<Template> findByTemplateType(TemplateType type);

    Template setStatus(int id);

    Template addZipFile(MultipartFile file);

    Template tree(int id);
}
