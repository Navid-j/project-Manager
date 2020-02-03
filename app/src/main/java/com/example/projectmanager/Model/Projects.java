package com.example.projectmanager.Model;

public class Projects {
    private int id;
    private String projectName;
    private String projectIntro;
    private String producerId;
    private String date;

    public Projects(int Id, String projectName, String projectIntro, String producerId, String date) {
        this.projectName = projectName;
        this.projectIntro = projectIntro;
        this.id = Id;
        this.producerId = producerId;
        this.date = date;
    }

    public String getProjectIntro() {
        return projectIntro;
    }

    public void setProjectIntro(String projectIntro) {
        this.projectIntro = projectIntro;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
