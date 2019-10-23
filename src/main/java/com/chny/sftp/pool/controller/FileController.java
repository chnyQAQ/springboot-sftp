package com.chny.sftp.pool.controller;

import com.chny.sftp.pool.config.SftpTemplate;
import com.chny.sftp.pool.config.SftpPoolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;

@RestController
public class FileController {

    @Autowired
    private SftpTemplate sftp;

    @PostMapping(path = "/upload")
    public void upload(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        Calendar calendar = Calendar.getInstance();
        // 年
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        // 月
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        // 目录相对路径
        String relativeDir = year + "-" + month + "/";
        try {
            // 上传到ftp
            sftp.upload(file.getInputStream(), relativeDir, name);
        } catch (Exception e) {
            throw new SftpPoolException("保存文件时出错", e);
        }
    }

    @GetMapping(path = "/download")
    public void downloadByte(HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            File file = sftp.downloadFile("file/2019-10/apache2.4最新安装手册.txt");
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

            byte[] buffer = new byte[1024];
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            throw new SftpPoolException("下载文件时出错", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

}
