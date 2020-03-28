package com.pzr.taoc.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class DataBean extends BmobObject {

    private int id;

    private String original;
    private String translate;
    private BmobFile voice;

    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BmobFile getVoice() {
        return voice;
    }

    public void setVoice(BmobFile voice) {
        this.voice = voice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

}
