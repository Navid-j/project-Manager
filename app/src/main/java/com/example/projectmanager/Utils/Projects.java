package com.example.projectmanager.Utils;

public class Projects {
    private String projectName;
    private String projectIntro;

    public Projects(String projectName, String projectIntro) {
        this.projectName = projectName;
        this.projectIntro = projectIntro;
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
}
