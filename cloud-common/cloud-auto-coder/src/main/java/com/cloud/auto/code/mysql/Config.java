package com.cloud.auto.code.mysql;

import lombok.Data;

import java.io.Serializable;

@Data
public class Config implements Serializable {

    String projectName;

    String url;

    String username;

    String password;

    String packagePath;

    String filePath;

    String auth;

    String dateTime;

    public Config(String projectName,String url, String username, String password, String packagePath, String filePath, String auth, String dateTime) {
        this.projectName = projectName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.packagePath = packagePath;
        this.filePath = filePath;
        this.auth = auth;
        this.dateTime = dateTime;
    }
}
