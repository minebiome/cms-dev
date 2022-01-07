package com.wangyang.common.utils;


//import com.vladsch.flexmark.ext.tables.TablesExtension;
//import com.vladsch.flexmark.ext.toc.TocExtension;

import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.media.tags.MediaTagsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
//import com.wangyang.common.flexmark.gitlab.GitLabExtension;
import com.wangyang.common.flexmark.gitlab.GitLabExtension;
import com.wangyang.pojo.entity.base.BaseArticle;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

//import com.wangyang.cms.gitlab.GitLabExtension;
//import com.wangyang.cms.media.tags.MediaTagsExtension;


public class MarkdownUtils {

    /**
     * 图片懒加载
     */
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS,Arrays.asList(
                    EmojiExtension.create(),
                    TablesExtension.create(),
                    GitLabExtension.create(),
                    TocExtension.create(),
                    MediaTagsExtension.create(),
                    FootnoteExtension.create(),
                    AdmonitionExtension.create()

            )) .set(GitLabExtension.IMAGE_SRC_TAG,"data-original")
            .set(HtmlRenderer.SOFT_BREAK, "<br/>")
//            .set(Parser.HARD_LINE_BREAK_LIMIT,true)
            .set(TocExtension.LEVELS, 255)
            .set(TocExtension.LIST_CLASS,"toc")
            .set(TocExtension.IS_NUMBERED,false)
            .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.EMOJI_CHEAT_SHEET)
                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.UNICODE_ONLY);



    private static final Parser PARSER = Parser.builder(OPTIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();


    public static BaseArticle renderHtml(BaseArticle article) {
        if(StringUtils.isBlank(article.getOriginalContent())){
            return null;
        }
        Node document = PARSER.parse("[TOC]\n @@@ \n\n"+article.getOriginalContent());

        String render = RENDERER.render(document);
        String[] split = render.split("<p>@@@</p>");
        article.setFormatContent(split[1]);
        article.setToc(split[0]);
        return article;
    }

    /**
     * 打印PDF
     */
    private static final DataHolder OPTIONS_OUTPUT = new MutableDataSet()
            .set(Parser.EXTENSIONS,Arrays.asList(
                    EmojiExtension.create(),
                    TablesExtension.create(),
                    GitLabExtension.create(),
                    TocExtension.create(),
                    MediaTagsExtension.create(),
                    FootnoteExtension.create(),
                    AdmonitionExtension.create()

            )).set(GitLabExtension.IMAGE_SRC_TAG,"src")
            .set(HtmlRenderer.SOFT_BREAK, "<br/>")
//            .set(Parser.HARD_LINE_BREAK_LIMIT,true)
            .set(TocExtension.LEVELS, 255)
            .set(TocExtension.LIST_CLASS,"toc")
            .set(TocExtension.IS_NUMBERED,false)
            .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.EMOJI_CHEAT_SHEET)
            .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.UNICODE_ONLY);
    private static final Parser PARSER_OUTPUT = Parser.builder(OPTIONS_OUTPUT).build();
    private static final HtmlRenderer RENDERER_OUTPUT = HtmlRenderer.builder(OPTIONS_OUTPUT).build();

    /**
     * 不使用图片懒加载
     * @param markdown
     * @return
     */
    public static String renderHtmlOutput(String markdown) {
        if(StringUtils.isBlank(markdown)){
            return null;
        }
        Node document = PARSER_OUTPUT.parse("[TOC]\n "+markdown);
        String render = RENDERER_OUTPUT.render(document);
        return render;
    }

    public  static String getText(String content){
        if(content==null){
            return StringUtils.EMPTY;
        }
        String txtcontent = content.replaceAll("</?[^>]+>", ""); //剔出<html>的标签  
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n|</a>", "");//去除字符串中的空格,回车,换行符,制表符
        return txtcontent;
    }

}
