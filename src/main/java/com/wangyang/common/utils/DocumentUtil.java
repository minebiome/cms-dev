package com.wangyang.common.utils;

import com.wangyang.pojo.dto.ContentTab;
import com.wangyang.pojo.entity.Sheet;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentUtil {

    public static  String getDivContent(String html,String id){
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select(id);
        if(rows.size()<=0){
//            throw new DocumentException("文档中没有找到id"+id);
            return StringUtils.EMPTY;
        }

        Element row = rows.get(0);
        return row.html();
    }

    public static  String addDebugLabel(String html){
        Document doc = Jsoup.parse(html);
        doc.body().append("<div style='    position: fixed;\n" +
                "    top: 118px;\n" +
                "    left: 5px;\n" +
                "    color: red;'>Debug!!</div>");
        return doc.html();
    }



    public static List<ContentTab> getContentTab(Sheet sheet){
        Document doc = Jsoup.parse(sheet.getFormatContent());
        Elements elements = doc.select("div[id^=cms]");
        List<ContentTab> contentTabs = new ArrayList<>();

        for (Element element : elements){
            String id = element.attr("id");
            String name = element.attr("data-name");
            ContentTab contentTab = new ContentTab();
            String path = sheet.getPath().replace("/", "_");

            contentTab.setId("/"+path+"_"+sheet.getViewName()+".html#"+id);
            contentTab.setName(name);
            contentTabs.add(contentTab);
        }
        return contentTabs;
//        Elements rows = doc.select(id);
//        if(rows.size()<=0){
////            throw new DocumentException("文档中没有找到id"+id);
//            return StringUtils.EMPTY;
//        }
//
//        Element row = rows.get(0);
//        return row.html();
    }
}
