package com.example.projectmanager.Model;

public class Messages {
    private int ID;
    private String messageIntro;
    private String mAttach;
    private String producerId;

    public Messages(int id, String messageIntro, String mAttach, String producerId) {
        this.ID = id;
        this.messageIntro = messageIntro;
        this.mAttach = mAttach;
        this.producerId = producerId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMessageIntro() {
        return messageIntro;
    }

    public void setMessageIntro(String messageIntro) {
        this.messageIntro = messageIntro;
    }

    public String ismAttach() {
        return mAttach;
    }

    public void setmAttach(String mAttach) {
        this.mAttach = mAttach;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }
}
