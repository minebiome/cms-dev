package com.wangyang.common.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.inline.IInliner;
import org.thymeleaf.inline.NoOpInliner;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.inline.*;
import org.thymeleaf.standard.processor.AbstractStandardTextInlineSettingTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public class CmsFileStandardInlineHTMLTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "inline";

    public CmsFileStandardInlineHTMLTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix,(String)null, false, "inline", true,1000,false);
    }
//    templateMode, dialectPrefix, (String)null, false, attrName, true, precedence, true

    private IInliner getInliner(ITemplateContext context, StandardInlineMode inlineMode) {
        switch(inlineMode) {
            case NONE:
                return NoOpInliner.INSTANCE;
            case HTML:
                return new StandardHTMLInliner(context.getConfiguration());
            case TEXT:
                return new StandardTextInliner(context.getConfiguration());
            case JAVASCRIPT:
                return new StandardJavaScriptInliner(context.getConfiguration());
            case CSS:
                return new StandardCSSInliner(context.getConfiguration());
            default:
                throw new TemplateProcessingException("Invalid inline mode selected: " + inlineMode + ". Allowed inline modes in template mode " + this.getTemplateMode() + " are: \"" + StandardInlineMode.HTML + "\", \"" + StandardInlineMode.TEXT + "\", \"" + StandardInlineMode.JAVASCRIPT + "\", \"" + StandardInlineMode.CSS + "\" and \"" + StandardInlineMode.NONE + "\"");
        }
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        IInliner inliner = this.getInliner(context, StandardInlineMode.parse(attributeValue));
        structureHandler.setInliner(inliner);
    }
}
