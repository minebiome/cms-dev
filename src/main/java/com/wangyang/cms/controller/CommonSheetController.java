package com.wangyang.cms.controller;

import com.wangyang.cms.service.ISheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sheet")
public class CommonSheetController {

    @Autowired
    ISheetService sheetService;
    @GetMapping("/preview/{id}")
    public ModelAndView preview(@PathVariable("id") Integer id){
        return sheetService.preview(id);
    }

}