package com.wangyang.pojo.enums;

import com.wangyang.common.enums.ValueEnum;

public enum TemplateData implements ValueEnum<Integer> {
    ARTICLE_TREE,
    ARTICLE_PAGE,
    CATEGORY_CHILD_PAGE, // 分类及文章
    CATEGORY_CHILD_TREE,
    OTHER;

    @Override
    public Integer getValue() {
        return null;
    }

//    private final int value;
//    private final String name;
//    TemplateData(int value,String name) {
//        this.value = value;
//        this.name = name;
//    }
//    List<TemplateTypeList> list =null;
//    public List<TemplateTypeList> getList(){
//        if(list!=null){
//            return list;
//        }
//        list = new ArrayList<TemplateTypeList>();
//        for (TemplateType templateType : TemplateType.values()){
//            TemplateTypeList templateTypeList = new TemplateTypeList();
//            templateTypeList.setId(templateType.getValue());
//            templateTypeList.setName(templateType.getName());
//            list.add(templateTypeList);
//        }
//        return list;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }


}
