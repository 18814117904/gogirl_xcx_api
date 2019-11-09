package com.gogirl.gogirl_service.utils;

import com.gogirl.gogirl_service.entity.Serve;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PictureUtils {

    public static void queryPicture(byte[] bypicture, HttpServletRequest request, HttpServletResponse response){
        OutputStream os = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bypicture));
            response.setContentType("image/png");
            os = response.getOutputStream();
            ImageIO.write(bufferedImage,"gif",os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void queryPictureScan(byte[] bypicture,int width, int height, HttpServletRequest request, HttpServletResponse response){
        BufferedImage src = null;
        Image image = null;
        BufferedImage tag = null;
        OutputStream os;
        try {
            src = ImageIO.read(new ByteArrayInputStream(bypicture));
            image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            tag = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            response.setContentType("image/png");
            os = response.getOutputStream();
            ImageIO.write(tag,"gif",os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPicture(MultipartFile upPicture, HttpServletRequest request, HttpServletResponse response){
        /*try {
            Serve serve = new Serve();
            byte[] p1 = upPicture.getBytes();
            serve.setPicture(p1);
            serveService.insertServe(serve);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
