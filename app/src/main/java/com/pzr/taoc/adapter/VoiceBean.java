package com.pzr.taoc.adapter;

public class VoiceBean {

    private String id;
    private String voiceName;
    private String voiceFile;

    public VoiceBean(String id, String voiceName, String voiceFile) {
        this.id = id;
        this.voiceName = voiceName;
        this.voiceFile = voiceFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }
}
