package com.chny.sftp.pool.config;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;

@Service
public class SftpTemplate {

    @Value("${sftp.rootDir:/upload}")
    private String rootDir;

    @Autowired
    private SftpPool pool;

    /**
     * 下载文件
     *
     * @param dir  远程目录
     * @param name 远程文件名
     * @return 文件字节数组
     */
    public byte[] download(String dir, String name) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            sftp.cd(dir);
            InputStream in = sftp.get(name);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*4];
            int n;
            while ((n = in.read(buffer))>0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    public File downloadFile(String targetPath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        OutputStream outputStream = null;
        try {
            sftp.cd(rootDir);
            File file = new File(targetPath.substring(targetPath.lastIndexOf("/") + 1));
            outputStream = new FileOutputStream(file);
            sftp.get(targetPath, outputStream);
            return file;
        } catch (SftpException e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            sftp.disconnect();
            pool.returnObject(sftp);
        }
    }

    /**
     * 上传文件
     *
     * @param dir  远程目录
     * @param name 远程文件名
     * @param in   输入流
     */
    public void upload(String dir, String name, InputStream in) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            // 创建目录时，已跳转到对应目录下
            makeDirs(dir, sftp);
            // sftp.cd(dir);
            sftp.put(in, name);
        } catch (SftpException e) {
            throw new SftpPoolException("sftp上传文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * @param file       上传文件
     * @param remotePath 服务器存放路径，支持多级目录
     * @throws
     */
    public void upload(File file, String remotePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        FileInputStream fileInputStream = null;
        try {
            if (file.isFile()) {
                sftp.cd(rootDir);
                makeDirs(remotePath, sftp);
                // sftp.cd(remotePath);
                fileInputStream = new FileInputStream(file);
                sftp.put(fileInputStream, file.getName());
            }
        } catch (SftpException e) {
            throw new SftpPoolException("上传sftp服务器错误", e);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            pool.returnObject(sftp);
        }

    }


    /**
     * @param file       上传文件
     * @param remoteName 上传文件名字
     * @param remotePath 服务器存放路径，支持多级目录
     * @throws
     */
    public boolean upload(File file, String remoteName, String remotePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        FileInputStream fileInputStream = null;
        try {
            if (file.isFile()) {
                sftp.cd(rootDir);
                makeDirs(remotePath, sftp);
                // sftp.cd(remotePath);
                fileInputStream = new FileInputStream(file);
                sftp.put(fileInputStream, remoteName);
                return true;
            }
        } catch (SftpException e) {
            throw new SftpPoolException("上传sftp服务器错误", e);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            pool.returnObject(sftp);
        }
        return false;
    }

    public boolean upload(InputStream inputStream, String remoteName, String remotePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            makeDirs(remotePath, sftp);
            // sftp.cd(remotePath);
            sftp.put(inputStream, remoteName);
            return true;
        } catch (SftpException e) {
            throw new SftpPoolException("上传sftp服务器错误", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            pool.returnObject(sftp);
        }
    }


    /**
     * 删除文件
     *
     * @param dir  远程目录
     * @param name 远程文件名
     */
    public void delete(String dir, String name) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            sftp.cd(dir);
            sftp.rm(name);
        } catch (SftpException e) {
            throw new SftpPoolException("sftp删除文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * 递归创建多级目录
     *
     * @param dir 多级目录
     */
    public void makeDirs(String dir, ChannelSftp sftp) throws SftpException {
        String[] paths = dir.split("/");
        for (String path : paths) {
            if (!StringUtils.isEmpty(path)) {
                if (!isDirectoryExist(path, sftp)) {
                    sftp.mkdir(path);
                }
                sftp.cd(path);
            }
        }
        // sftp.cd(rootDir);
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirectoryExist(String directoryPath, ChannelSftp sftp) {
        boolean directoryExist = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directoryPath);
            directoryExist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                directoryExist = false;
            }
        }
        return directoryExist;
    }

}
