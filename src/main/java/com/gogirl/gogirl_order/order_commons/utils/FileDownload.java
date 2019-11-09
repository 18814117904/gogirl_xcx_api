package com.gogirl.gogirl_order.order_commons.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yinyong on 2018/9/21.
 */
public class FileDownload {

    public ResponseEntity<InputStreamResource> download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileSystemResource file = new FileSystemResource("C:\\Users\\Administrator\\Desktop\\web\\index.html");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        // new String(fileName.getBytes("UTF-8"),"iso-8859-1") 解决文件下载的时候文件名乱码的问题
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String("shouye.html".getBytes("UTF-8"),"iso-8859-1")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
