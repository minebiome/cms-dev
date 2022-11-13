package com.wangyang.common.flexmark.footnotes;

import com.vladsch.flexmark.parser.block.*;
import com.vladsch.flexmark.util.ast.Block;
import com.vladsch.flexmark.util.ast.BlockContent;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.wangyang.common.flexmark.footnotes.internal.*;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.KeepType;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.DataKey;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.format.options.ElementPlacement;
import com.vladsch.flexmark.util.format.options.ElementPlacementSort;
import com.wangyang.service.IArticleService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extension for footnotes
 * <p>
 * Create it with {@link #create()} and then configure it on the builders
 * <p>
 * The parsed footnote references in text regions are turned into {@link Footnote} nodes.
 * The parsed footnote definitions are turned into {@link FootnoteBlock} nodes.
 */

public class FootnoteExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension, Parser.ReferenceHoldingExtension, Formatter.FormatterExtension {
    final public static DataKey<KeepType> FOOTNOTES_KEEP = new DataKey<>("FOOTNOTES_KEEP", KeepType.FIRST);
    @Autowired
    static IArticleService articleService;
    final public static DataKey<FootnoteRepository> FOOTNOTES = new DataKey<>("FOOTNOTES", new FootnoteRepository(null), FootnoteRepository::new);

    public  void setArticleService(IArticleService articleService) {
        articleService = articleService;

    }

    final public static DataKey<String> FOOTNOTE_REF_PREFIX = new DataKey<>("FOOTNOTE_REF_PREFIX", "[");
    final public static DataKey<String> FOOTNOTE_REF_SUFFIX = new DataKey<>("FOOTNOTE_REF_SUFFIX", "]");
    final public static DataKey<String> FOOTNOTE_BACK_REF_STRING = new DataKey<>("FOOTNOTE_BACK_REF_STRING", "&#8617;");
    final public static DataKey<String> FOOTNOTE_LINK_REF_CLASS = new DataKey<>("FOOTNOTE_LINK_REF_CLASS", "footnote-ref");
    final public static DataKey<String> FOOTNOTE_BACK_LINK_REF_CLASS = new DataKey<>("FOOTNOTE_BACK_LINK_REF_CLASS", "footnote-backref");

    // formatter options
    final public static DataKey<ElementPlacement> FOOTNOTE_PLACEMENT = new DataKey<>("FOOTNOTE_PLACEMENT", ElementPlacement.AS_IS);
    final public static DataKey<ElementPlacementSort> FOOTNOTE_SORT = new DataKey<>("FOOTNOTE_SORT", ElementPlacementSort.AS_IS);
    static String FOOTNOTE_ID = ".*";
    static Pattern FOOTNOTE_DEF_PATTERN = Pattern.compile("^\\[\\^\\s*(" + FOOTNOTE_ID + ")\\s*\\]:");

    private FootnoteExtension() {
    }

    public static FootnoteExtension create() {
        return new FootnoteExtension();
    }

    @Override
    public void extend(Formatter.Builder formatterBuilder) {
        formatterBuilder.nodeFormatterFactory(new FootnoteNodeFormatter.Factory());
    }

    @Override
    public void rendererOptions(@NotNull MutableDataHolder options) {

    }

    @Override
    public void parserOptions(MutableDataHolder options) {

    }

    @Override
    public boolean transferReferences(MutableDataHolder document, DataHolder included) {
        if (document.contains(FOOTNOTES) && included.contains(FOOTNOTES)) {
            return Parser.transferReferences(FOOTNOTES.get(document), FOOTNOTES.get(included), FOOTNOTES_KEEP.get(document) == KeepType.FIRST);
        }
        return false;
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new FootnoteBlockParser.Factory());

        parserBuilder.linkRefProcessorFactory(new FootnoteLinkRefProcessor.Factory());
//        parserBuilder.customBlockParserFactory(new CustomBlockParserFactory() {
//            @Override
//            public @NotNull BlockParserFactory apply(@NotNull DataHolder optionsParam) {
//                return new AbstractBlockParserFactory(optionsParam){
//                    final private FootnoteOptions options = new FootnoteOptions(optionsParam);;
//
//                    @Override
//                    public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
//                        if (state.getIndent() >= 4) {
//                            return BlockStart.none();
//                        }
//
//                        BasedSequence line = state.getLine();
//                        int nextNonSpace = state.getNextNonSpaceIndex();
//                        BasedSequence trySequence = line.subSequence(nextNonSpace, line.length());
//                        Matcher matcher = FOOTNOTE_DEF_PATTERN.matcher(trySequence);
//                        if (matcher.find()) {
//                            // abbreviation definition
//                            int openingStart = nextNonSpace + matcher.start();
//                            int openingEnd = nextNonSpace + matcher.end();
//                            BasedSequence openingMarker = line.subSequence(openingStart, openingStart + 2);
//                            BasedSequence text = line.subSequence(openingStart + 2, openingEnd - 2).trim();
//                            BasedSequence closingMarker = line.subSequence(openingEnd - 2, openingEnd);
////                            options.
//                            int contentOffset = options.contentIndent;
//                            final FootnoteBlock block = new FootnoteBlock();
//
//                            block.setOpeningMarker(openingMarker);
//                            block.setText(text);
//                            block.setClosingMarker(closingMarker);
//
//                            AbstractBlockParser blockParser = new AbstractBlockParser() {
//                                BlockContent content = new BlockContent();
//                                @Override
//                                public Block getBlock() {
//                                    return block;
//                                }
//                                public BlockContent getBlockContent() {
//                                    return content;
//                                }
//                                @Override
//                                public BlockContinue tryContinue(ParserState parserState) {
//                                    final int nonSpaceIndex = state.getNextNonSpaceIndex();
//                                    if (state.isBlank()) {
//                                        if (block.getFirstChild() == null) {
//                                            // Blank line after empty list item
//                                            return BlockContinue.none();
//                                        } else {
//                                            return BlockContinue.atIndex(nonSpaceIndex);
//                                        }
//                                    }
//
//                                    if (state.getIndent() >= options.contentIndent) {
//                                        int contentIndent = state.getIndex() + options.contentIndent;
//                                        return BlockContinue.atIndex(contentIndent);
//                                    } else {
//                                        return BlockContinue.none();
//                                    }
//                                }
//                                @Override
//                                public void addLine(ParserState state, BasedSequence line) {
//                                    content.add(line, state.getIndent());
//                                }
//                                @Override
//                                public void closeBlock(ParserState parserState) {
//                                    // set the footnote from closingMarker to end
//                                    block.setCharsFromContent();
//                                    block.setFootnote(block.getChars().subSequence(block.getClosingMarker().getEndOffset() - block.getStartOffset()).trimStart());
//                                    // add it to the map
//                                    FootnoteRepository footnoteMap = FootnoteExtension.FOOTNOTES.get(state.getProperties());
//                                    footnoteMap.put(footnoteMap.normalizeKey(block.getText()), block);
//                                    content = null;
//                                }
//                                @Override
//                                public boolean isContainer() {
//                                    return true;
//                                }
//                                @Override
//                                public boolean canContain(ParserState state, BlockParser blockParser, Block block) {
//                                    return true;
//                                }
//                            };
//
////                            FootnoteBlockParser footnoteBlockParser = new FootnoteBlockParser(options, contentOffset);
////                            footnoteBlockParser.block.setOpeningMarker(openingMarker);
////                            footnoteBlockParser.block.setText(text);
////                            footnoteBlockParser.block.setClosingMarker(closingMarker);
//
//                            return BlockStart.of(blockParser)
//                                    .atIndex(openingEnd);
////                            return BlockStart.none();
//                        } else {
//                            return BlockStart.none();
//                        }
//                    }
//                };
//            }
//
//            @Override
//            public @Nullable Set<Class<?>> getAfterDependents() {
//                return null;
//            }
//
//            @Override
//            public @Nullable Set<Class<?>> getBeforeDependents() {
//                return null;
//            }
//
//            @Override
//            public boolean affectsGlobalScope() {
//                return false;
//            }
//        });

    }

    @Override
    public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
        if (htmlRendererBuilder.isRendererType("HTML")) {
            htmlRendererBuilder.nodeRendererFactory(new FootnoteNodeRenderer.Factory());
        } else if (htmlRendererBuilder.isRendererType("JIRA")) {
        }
    }




}
