package com.wangyang.common.thymeleaf;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.EscapedAttributeUtils;
import org.thymeleaf.util.Validate;

/**
 *  <span cms:classappend="${URL== item.name }? active : '' " >fewag</span>
 *  这里需要在file 引擎中解析item.name
 */
public class CmsClassappendTagProcessor extends AbstractStandardExpressionAttributeTagProcessor implements IAttributeDefinitionsAware {
    public static final int PRECEDENCE = 1100;
    public static final String ATTR_NAME = "myclassappend";
    public static final String TARGET_ATTR_NAME = "class";
    private static final TemplateMode TEMPLATE_MODE;
    private AttributeDefinition targetAttributeDefinition;

    public CmsClassappendTagProcessor(String dialectPrefix) {
        super(TEMPLATE_MODE, dialectPrefix, "myclassappend", 1100, true, false);
    }

    public void setAttributeDefinitions(AttributeDefinitions attributeDefinitions) {
        Validate.notNull(attributeDefinitions, "Attribute Definitions cannot be null");
        this.targetAttributeDefinition = attributeDefinitions.forName(TEMPLATE_MODE, "class");
    }

    protected final void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, Object expressionResult, IElementTagStructureHandler structureHandler) {
//        String newAttributeValue = EscapedAttributeUtils.escapeAttribute(this.getTemplateMode(), expressionResult == null ? null : expressionResult.toString());
        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, "${item.urlName}");
        Object execute = expression.execute(context);
//        structureHandler.setAttribute("cms:replace",execute.toString());
        structureHandler.setAttribute("cms:classappend","${URL== '"+execute.toString()+"' }? active : '' ");
//        System.out.println();
//        if (newAttributeValue != null && newAttributeValue.length() > 0) {
//            AttributeName targetAttributeName = this.targetAttributeDefinition.getAttributeName();
//            if (tag.hasAttribute(targetAttributeName)) {
//                String currentValue = tag.getAttributeValue(targetAttributeName);
//                if (currentValue.length() > 0) {
//                    newAttributeValue = currentValue + ' ' + newAttributeValue;
//                }
//            }
//
//            StandardProcessorUtils.setAttribute(structureHandler, this.targetAttributeDefinition, "class", newAttributeValue);
//        }

    }

    static {
        TEMPLATE_MODE = TemplateMode.HTML;
    }
}
