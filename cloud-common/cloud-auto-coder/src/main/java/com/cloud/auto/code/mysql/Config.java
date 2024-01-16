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

    String javaFilePath;

    String webFilePath;

    String webPackagePath;

    String auth;

    String dateTime;

    public Config(String projectName,String url, String username, String password, String packagePath, String javaFilePath,
                  String webFilePath, String auth, String dateTime) {
        this.projectName = projectName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.packagePath = packagePath;
        this.javaFilePath = javaFilePath;
        this.webFilePath = webFilePath;
        this.auth = auth;
        this.dateTime = dateTime;
    }
}
