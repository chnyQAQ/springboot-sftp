package com.chny.sftp.pool.controller;

import com.chny.sftp.pool.config.support.Sftp;
import com.chny.sftp.pool.exception.SftpPoolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@RestController
public class FileController {

    @Autowired
    private Sftp sftp;

    @PostMapping(path = "/upload")
    public void upload(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        Calendar calendar = Calendar.getInstance();
        // 年
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        // 月
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        // 目录相对路径
        String relativeDir = year + "-" + month;
        // 目录绝对路径
        String absoluteDir = "/file/" + relativeDir;
        try {
            // 上传到ftp
            sftp.upload(absoluteDir, name, file.getInputStream());
        } catch (IOException e) {
            throw new SftpPoolException("保存文件时出错", e);
        }
    }

    @GetMapping(path = "/download")
    public void downloadByte(HttpServletResponse response) throws IOException {
        FileCopyUtils.copy(sftp.download("/file/2019-9/", "camel.txt"), response.getOutputStream());
    }

}
