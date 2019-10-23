package com.chny.sftp.pool.config;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

@Service
public class SftpTemplate {

    @Value("${sftp.rootDir:/upload}")
    private String rootDir;

    @Autowired
    private SftpPool pool;

    public FileInfo getFileInfo(String fileName) {
        try {
            File file = downloadFile(fileName);
            return file == null ? null : new FileInfo(file.getName(), (int) file.getTotalSpace(), new Date(file.lastModified()), file.isDirectory());
        } catch (IOException e) {
            throw new SftpPoolException("获取文件信息异常", e);
        }
    }

    public List<FileInfo> getFileInfos(String directoryPath) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            Vector<ChannelSftp.LsEntry> files = sftp.ls(directoryPath);
            List<FileInfo> fileList = null;
            if (null != files) {
                fileList = new ArrayList<>();
                Iterator<ChannelSftp.LsEntry> iterator = files.iterator();
                while (iterator.hasNext()) {
                    ChannelSftp.LsEntry next = iterator.next();
                    String fileName = next.getFilename();
                    if (!".".equals(fileName) && !"..".equals(fileName)) {
                        Integer size = (int) next.getAttrs().getSize();
                        long updateTime = next.getAttrs().getMTime();
                        boolean isDirectory = false;
                        if (String.valueOf(next.getLongname()).startsWith("d")) {
                            isDirectory = true;
                        }
                        fileList.add(new FileInfo(fileName, size, new Date(updateTime), isDirectory));
                    }
                }
            }
            return fileList;
        } catch (SftpException e) {
            throw new SftpPoolException("获取目录列表 channel.ls " + directoryPath + "失败 " + e);
        }
    }

    /**
     * 下载文件
     *
     * @param dirPath  目录
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public byte[] download(String dirPath, String fileName) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            sftp.cd(dirPath);
            InputStream in = sftp.get(fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n;
            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
            sftp.cd(rootDir);
            return out.toByteArray();
        } catch (Exception e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件字节数组
     */
    public byte[] download(String filePath) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            int index = filePath.lastIndexOf("/");
            sftp.cd(filePath.substring(0, index));
            InputStream in = sftp.get(filePath.substring(index + 1));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n;
            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
            sftp.cd(rootDir);
            return out.toByteArray();
        } catch (Exception e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * 下载文件
     *
     * @param dirPath  目录
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public File downloadFile(String dirPath, String fileName) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        OutputStream outputStream = null;
        try {
            sftp.cd(rootDir);
            File file = new File(fileName);
            outputStream = new FileOutputStream(file);
            sftp.get(dirPath + "/" + fileName, outputStream);
            sftp.cd(rootDir);
            return file;
        } catch (SftpException e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            pool.returnObject(sftp);
        }
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public File downloadFile(String filePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        OutputStream outputStream = null;
        try {
            sftp.cd(rootDir);
            File file = new File(filePath.substring(filePath.lastIndexOf("/") + 1));
            outputStream = new FileOutputStream(file);
            sftp.get(filePath, outputStream);
            sftp.cd(rootDir);
            return file;
        } catch (SftpException e) {
            throw new SftpPoolException("sftp下载文件出错", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            pool.returnObject(sftp);
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param dirPath     目录
     * @param fileName    文件名
     */
    public void upload(InputStream inputStream, String dirPath, String fileName) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            makeDirs(dirPath, sftp);
            sftp.put(inputStream, fileName);
            sftp.cd(rootDir);
        } catch (SftpException e) {
            throw new SftpPoolException("sftp上传文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * @param file     上传文件
     * @param filePath 文件路径，支持多级目录
     * @throws
     */
    public void upload(File file, String filePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        FileInputStream fileInputStream = null;
        try {
            if (file.isFile()) {
                sftp.cd(rootDir);
                int index = filePath.lastIndexOf("/");
                makeDirs(filePath.substring(0, index), sftp);
                fileInputStream = new FileInputStream(file);
                sftp.put(fileInputStream, filePath.substring(index + 1));
            }
            sftp.cd(rootDir);
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
     * @param file     上传文件
     * @param fileName 文件名
     * @param fileDir  文件路径，支持多级目录
     * @throws
     */
    public boolean upload(File file, String fileName, String fileDir) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        FileInputStream fileInputStream = null;
        try {
            if (file.isFile()) {
                sftp.cd(rootDir);
                makeDirs(fileDir, sftp);
                // sftp.cd(remotePath);
                fileInputStream = new FileInputStream(file);
                sftp.put(fileInputStream, fileName);
                return true;
            }
            sftp.cd(rootDir);
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

    /**
     * @param inputStream 输入流
     * @param filePath    文件路径
     * @throws
     */
    public boolean upload(InputStream inputStream, String filePath) throws IOException {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            makeDirs(filePath, sftp);
            // sftp.cd(remotePath);
            sftp.put(inputStream, filePath);
            sftp.cd(rootDir);
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
     * @param fileDir  目录
     * @param fileName 文件名
     */
    public void delete(String fileDir, String fileName) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            sftp.cd(fileDir);
            sftp.rm(fileName);
            sftp.cd(rootDir);
        } catch (SftpException e) {
            throw new SftpPoolException("sftp删除文件出错", e);
        } finally {
            pool.returnObject(sftp);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public void delete(String filePath) {
        ChannelSftp sftp = pool.borrowObject();
        try {
            sftp.cd(rootDir);
            sftp.rm(filePath);
            sftp.cd(rootDir);
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
    private void makeDirs(String dir, ChannelSftp sftp) throws SftpException {
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
    private boolean isDirectoryExist(String directoryPath, ChannelSftp sftp) {
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
