package com.wangyang.common.flexmark.footnotes;

public interface FootnoteVisitor {
    void visit(FootnoteBlock node);
    void visit(Footnote node);
}
