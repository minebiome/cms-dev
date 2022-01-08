package com.wangyang.syscall.controller;


import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ArticleException;
import com.wangyang.pojo.annotation.Anonymous;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Sheet;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ISheetService;
import com.wangyang.syscall.utils.NodeJsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

@Controller
@RequestMapping("/download")
@Slf4j
public class DownloadController {

    @Autowired
    IArticleService articleService;

    @Autowired
    ISheetService sheetService;

    @GetMapping("/sheet/{id}")
    @Anonymous
    public String  generateSheetPdf(@PathVariable("id") Integer id) {
        Sheet sheet = sheetService.findById(id);

        if(sheet.getStatus()!= ArticleStatus.PUBLISHED){
            throw new ArticleException("文章没有发布不能生成PDF!!");
        }
        String pdfPath= sheet.getPath()+File.separator+sheet.getViewName()+".pdf";
        String absolutePath = CmsConst.WORK_DIR +File.separator+pdfPath;
        File file = new File(absolutePath);

        if(!file.exists()||sheet.getPdfPath()==null){
            String url = "http://localhost:8080/preview/sheet/"+id;
            String node = NodeJsUtil.execNodeJs("node", CmsConst.WORK_DIR + "/templates/nodejs/generatePdf.js", url, CmsConst.WORK_DIR+File.separator+pdfPath);
            sheet.setPdfPath(pdfPath);
            sheet = sheetService.save(sheet);
            log.info("生成：{}",pdfPath);
        }else {
            log.info("使用原来：{}",pdfPath);

        }        return "redirect:/"+sheet.getPdfPath();
    }
    @GetMapping("/article/{articleId}")
    public String  generatePdf(@PathVariable("articleId") Integer articleId) {
        Article article = articleService.findArticleById(articleId);
        if(article.getStatus()!= ArticleStatus.PUBLISHED){
            throw new ArticleException("文章没有发布不能生成PDF!!");
        }
        String pdfPath= article.getPath()+File.separator+article.getViewName()+".pdf";
        String absolutePath = CmsConst.WORK_DIR +File.separator+pdfPath;
        File file = new File(absolutePath);
        if(!file.exists()||article.getPdfPath()==null){
            String url = "http://localhost:8080/preview/pdf/"+articleId;
            String node = NodeJsUtil.execNodeJs("node", CmsConst.WORK_DIR + "/templates/nodejs/generatePdf.js", url, CmsConst.WORK_DIR+File.separator+pdfPath);
            article.setPdfPath(pdfPath);
            article = articleService.save(article);
        }
        return "redirect:/"+article.getPdfPath();
    }
}
