package com.app.recyclerviewdemo;

import com.app.recyclerviewdemo.base.BaseModel;

public class Model  extends BaseModel {

    private String content;

    public Model(String content, int type) {
        this.content = content;
        super.setViewType(type);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
