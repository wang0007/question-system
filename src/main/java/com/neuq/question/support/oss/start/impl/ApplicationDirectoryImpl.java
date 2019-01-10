package com.neuq.question.support.oss.start.impl;

/**
 * @author wangshyi
 * @date 2019/1/2  10:30
 */
import com.neuq.question.error.ECUnauthorizedException;
import com.neuq.question.support.oss.start.ApplicationDirectory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author liuhaoi
 */
@Component
public class ApplicationDirectoryImpl implements ApplicationDirectory {

    private ApplicationHome home = new ApplicationHome();

    private File tempRootDir;

    @Override
    public String getHomeDirectoryPath() {
    return getHomeDirectory().getAbsolutePath();
    }

    @Override
    public File getHomeDirectory() {
    return home.getDir();
    }

    @Override
    public String getTempDirectoryPath() {
    return getTempDirectory().getAbsolutePath();
    }

    @Override
    public File getTempDirectory() {

    if (tempRootDir != null) {
    checkRootDirectoryExists();
    return tempRootDir;
    }

    File dir = home.getDir();

    File temp = new File(dir, "temp");

    if (temp.exists() && temp.isDirectory() && temp.canWrite()) {
    tempRootDir = temp;
    return temp;
    }

    if (!temp.exists()) {
    temp.mkdirs();
    tempRootDir = temp;
    return temp;
    }

    temp = new File(dir, "temp_" + System.currentTimeMillis());
    temp.mkdirs();
    tempRootDir = temp;
    return temp;
    }

    private void checkRootDirectoryExists() {

    if (tempRootDir != null && !tempRootDir.exists()) {
    tempRootDir.mkdirs();
    }
    }

    @Override
    public String getTempDirectoryPath(String subDirectoryName) {
    return getTempDirectory(subDirectoryName).getAbsolutePath();
    }

    @Override
    public File getTempDirectory(String subDirectoryName) {

    File temp = new File(getTempDirectory(), subDirectoryName);

    if (temp.exists() && temp.isDirectory() && temp.canWrite()) {
    return temp;
    }

    if (!temp.exists()) {
    temp.mkdirs();
    return temp;
    }

    throw new ECUnauthorizedException("has no access to write file to " + temp.getAbsolutePath());

    }


}

