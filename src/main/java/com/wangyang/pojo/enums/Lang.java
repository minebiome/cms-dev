package com.wangyang.pojo.enums;

public enum Lang implements ValueEnum<Integer>{
    ZH(""),EN("en");
    private final  String suffix;


    Lang(String suffix) {
        this.suffix = suffix;
    }
    public String getSuffix() {
        return suffix;
    }

    @Override
    public Integer getValue() {
        return null;
    }
}
