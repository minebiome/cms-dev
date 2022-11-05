package com.wangyang.web.controller.api;

import com.wangyang.common.BaseResponse;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.pojo.entity.Menu;
import com.wangyang.service.ILiteratureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/literature")
public class LiteratureController {
    @Autowired
    ILiteratureService literatureService;
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
}
