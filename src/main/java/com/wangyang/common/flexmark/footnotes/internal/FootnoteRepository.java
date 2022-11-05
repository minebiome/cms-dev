package com.wangyang.common.flexmark.footnotes.internal;

import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.wangyang.common.flexmark.footnotes.Footnote;
import com.wangyang.common.flexmark.footnotes.FootnoteBlock;
import com.wangyang.common.flexmark.footnotes.FootnoteExtension;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.DataKey;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.config.ApplicationBean;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Literature;
import com.wangyang.service.IArticleService;
import com.wangyang.service.ILiteratureService;
import com.wangyang.service.impl.ArticleServiceImpl;
import com.wangyang.service.impl.LiteratureServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@SuppressWarnings("WeakerAccess")


public class FootnoteRepository extends NodeRepository<FootnoteBlock> {
    private ArrayList<FootnoteBlock> referencedFootnoteBlocks = new ArrayList<>();


    public FootnoteRepository(DataHolder options) {
        super(FootnoteExtension.FOOTNOTES_KEEP.get(options));
    }
    public static void resolveFootnotes(Document document) {
        FootnoteRepository footnoteRepository = FootnoteExtension.FOOTNOTES.get(document);
        
        boolean[] hadNewFootnotes = { false };
        NodeVisitor visitor = new NodeVisitor(
                new VisitHandler<>(Footnote.class, node -> {
                    if (!node.isDefined()) {
                        FootnoteBlock footonoteBlock = node.getFootnoteBlock(footnoteRepository);

                        if (footonoteBlock != null) {
                            footnoteRepository.addFootnoteReference(footonoteBlock, node);
                            node.setFootnoteBlock(footonoteBlock);
                            hadNewFootnotes[0] = true;
                        }
                    }
                })
        );

        visitor.visit(document);
        if (hadNewFootnotes[0]) {
            footnoteRepository.resolveFootnoteOrdinals();
        }
    }

//    public void addFootnoteReference(FootnoteBlock footnoteBlock, Footnote footnote) {
//
//        if (!footnoteBlock.isReferenced()) {
//            referencedFootnoteBlocks.add(footnoteBlock);
//        }
//
//        footnoteBlock.setFirstReferenceOffset(footnote.getStartOffset());
//
//        int referenceOrdinal = footnoteBlock.getFootnoteReferences();
//        footnoteBlock.setFootnoteReferences(referenceOrdinal + 1);
//        footnote.setReferenceOrdinal(referenceOrdinal);
//    }
    public void addFootnoteReference(FootnoteBlock footnoteBlock, Footnote footnote) {

        referencedFootnoteBlocks.add(footnoteBlock);
        footnoteBlock.setFootnote(BasedSequence.of(footnote.getText()));

        footnoteBlock.setFirstReferenceOffset(footnote.getStartOffset());

        int referenceOrdinal = footnoteBlock.getFootnoteReferences();
        footnoteBlock.setFootnoteReferences(referenceOrdinal + 1);
        footnote.setReferenceOrdinal(referenceOrdinal);
    }

    public void resolveFootnoteOrdinals() {
        // need to sort by first referenced offset then set each to its ordinal position in the array+1
        Collections.sort(referencedFootnoteBlocks, (f1, f2) -> f1.getFirstReferenceOffset() - f2.getFirstReferenceOffset());

        Set<BasedSequence> sequences = ServiceUtil.fetchProperty(referencedFootnoteBlocks, FootnoteBlock::getFootnote);
        IArticleService articleService = ApplicationBean.getBean(ArticleServiceImpl.class);
        ILiteratureService literatureService = ApplicationBean.getBean(LiteratureServiceImpl.class);

        Set<Integer> articleIds = new HashSet<>();
        Set<Integer> literatureIds = new HashSet<>();
        Set<String> literatureStrIds = new HashSet<>();
        for(BasedSequence sequence : sequences){
            String idStr = sequence.toString();
            if(idStr.startsWith("A")){
                try {
                    int id = Integer.parseInt(idStr.replace("A", ""));
                    articleIds.add(id);
                } catch (NumberFormatException e) {
                    continue;
                }

            }else if(idStr.startsWith("L")){
                try {
                    int id = Integer.parseInt(idStr.replace("L", ""));
                    literatureIds.add(id);
                } catch (NumberFormatException e) {
                    continue;
                }
            }else if(idStr.startsWith("SL:")){
                literatureStrIds.add(idStr.replace("SL:", ""));
            }else {
                continue;
            }
        }
        Map<Integer, Article> articleMap=new HashMap<>();
        if(articleIds.size()>0){
            List<Article> articles = articleService.listByIds(articleIds);
            articleMap= ServiceUtil.convertToMap(articles, Article::getId);

        }
        Map<Integer, Literature> literatureMap = new HashMap<>();
        if(literatureIds.size()>0){
            List<Literature> literatures = literatureService.listByIds(literatureIds);
            literatureMap = ServiceUtil.convertToMap(literatures, Literature::getId);
        }

        Map<String, Literature> literatureStrMap = new HashMap<>();
        if(literatureStrIds.size()>0){
            List<Literature> literatures = literatureService.listByKeys(literatureStrIds);
            literatureStrMap = ServiceUtil.convertToMap(literatures, Literature::getKey);
        }

        int ordinal = 0;
        List<FootnoteBlock> removeFootnoteBlock = new ArrayList<>();
        for (int i = 0;i<referencedFootnoteBlocks.size();i++) {
            FootnoteBlock footnoteBlock=referencedFootnoteBlocks.get(i);
            String idStr = footnoteBlock.getFootnote().toString();
            if(idStr.startsWith("A")){
                int id = 0;
                try {
                    id = Integer.parseInt(idStr.replace("A", ""));
                } catch (NumberFormatException e) {
                    referencedFootnoteBlocks.remove(footnoteBlock);
                    continue;
                }
                Article article = articleMap.get(id);
                if(article==null){
                    removeFootnoteBlock.add(footnoteBlock);
                    continue;
                }
                footnoteBlock.setFootnote(BasedSequence.of(article.getTitle()));
                footnoteBlock.setUrl(article.getViewName());


            }else if(idStr.startsWith("L")){
                int id = 0;
                try {
                    id = Integer.parseInt(idStr.replace("L", ""));
                } catch (NumberFormatException e) {
                    referencedFootnoteBlocks.remove(footnoteBlock);
                    continue;
                }
                Literature literature = literatureMap.get(id);
                if(literature==null){
                    removeFootnoteBlock.add(footnoteBlock);
                    continue;
                }
                footnoteBlock.setFootnote(BasedSequence.of(literature.getTitle()));
                footnoteBlock.setUrl(literature.getUrl());

            }else if(idStr.startsWith("SL:")){
                String id = idStr.replace("SL:", "");
                Literature literature = literatureStrMap.get(id);
                if(literature==null){
                    removeFootnoteBlock.add(footnoteBlock);
                    continue;
                }
                footnoteBlock.setFootnote(BasedSequence.of(literature.getTitle()));
                footnoteBlock.setUrl(literature.getUrl());
            }else {
                removeFootnoteBlock.add(footnoteBlock);
                continue;
            }
            footnoteBlock.setFootnoteOrdinal(++ordinal +"");
        }
        referencedFootnoteBlocks.removeAll(removeFootnoteBlock);

    }

    public List<FootnoteBlock> getReferencedFootnoteBlocks() {
        return referencedFootnoteBlocks;
    }



    @NotNull
    @Override
    public DataKey<FootnoteRepository> getDataKey() {
        return FootnoteExtension.FOOTNOTES;
    }

    @NotNull
    @Override
    public DataKey<KeepType> getKeepDataKey() {
        return FootnoteExtension.FOOTNOTES_KEEP;
    }

    @NotNull
    @Override
    public Set<FootnoteBlock> getReferencedElements(Node parent) {
        HashSet<FootnoteBlock> references = new HashSet<>();
        visitNodes(parent, value -> {
            if (value instanceof Footnote) {
                FootnoteBlock reference = ((Footnote) value).getReferenceNode(FootnoteRepository.this);
                if (reference != null) {
                    references.add(reference);
                }
            }
        }, Footnote.class);
        return references;
    }

    @Override
    public @Nullable FootnoteBlock get(@NotNull Object o) {
//        return super.get(o);
        String name = (String)o;
        IArticleService articleService = ApplicationBean.getBean(ArticleServiceImpl.class);
        Article article = articleService.findByViewName(name);

        FootnoteBlock footnoteBlock = new FootnoteBlock();
//        footnoteBlock.setText(BasedSequence.of("bbbbb"));
        footnoteBlock.setFootnote(BasedSequence.of(article.getTitle()));
        return footnoteBlock;

    }
}
