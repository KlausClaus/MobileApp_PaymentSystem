package com.example.payment.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Controller for handling file upload and download operations.
 * This controller provides REST APIs for uploading files to the server and downloading them
 * by their unique identifier.
 */
@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * Path where files will be uploaded on the server.
     */
    @Value("${files.upload.path}")
    private String fileUploadPath;

    /**
     * URL for accessing uploaded files.
     */
    String url;

    /**
     * Handles file upload requests.
     * The uploaded file is saved to the configured upload path with a unique identifier
     * and the URL to access the file is returned.
     *
     * @param file The file to be uploaded.
     * @return The URL of the uploaded file.
     * @throws IOException If an error occurs during file upload.
     */
    @PostMapping("/file")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);

        File uploadParentFile = new File(fileUploadPath);
        System.out.println(fileUploadPath);
        if (!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath+fileUUID);
        file.transferTo(uploadFile);

        url = "http://172.20.10.2:9090/files/"+fileUUID;
        System.out.println("--------------"+url);
        return url;
    }



    /**
     * Handles file download requests.
     * The file is identified by its unique identifier (UUID) and is served to the client
     * as an attachment.
     *
     * @param fileUUID The unique identifier of the file to be downloaded.
     * @param response The HTTP response to write the file data.
     * @throws IOException If an error occurs during file download.
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // Retrieve the file based on its unique identifier
        File uploadFile = new File(fileUploadPath + fileUUID);
        // Set the response headers and content type
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // Write the file's byte data to the output stream
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }



}
