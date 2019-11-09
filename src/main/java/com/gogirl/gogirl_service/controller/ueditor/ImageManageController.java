package com.gogirl.gogirl_service.controller.ueditor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.entity.ImageGroup;
import com.gogirl.gogirl_service.entity.ImageManage;
import com.gogirl.gogirl_service.service.service.image.ImageGroupService;
import com.gogirl.gogirl_service.service.service.image.ImageManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/11.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "image")
public class ImageManageController {

    @Autowired
    private ImageManageService imageManageService;

    @Autowired
    private ImageGroupService imageGroupService;

    @Value("${picture-path}")
    private String picturePath;

    @Value("${picture-prefix}")
    private String picturePrefix;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryImageForPage")
    public Map<String, Object> queryImageForPage(ImageManage imageManage, HttpServletRequest request, HttpServletResponse response){
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        List<ImageManage> list = imageManageService.listImageManageForPage(imageManage);
        PageInfo<ImageManage> pageInfo = new PageInfo<ImageManage>(list);
        map.put("code","success");
        map.put("pageInfo",pageInfo);
        map.put("message",list);
        return map;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryImageGroup")
    public Map<String, Object> queryImageGroup(){
        Map<String, Object> map = new HashMap<String, Object>();
        List<ImageGroup> list = imageGroupService.listImageGroup();
        map.put("code","success");
        map.put("message",list);
        return map;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="addPicture")
    public JsonResult addPicture (@RequestBody List<ImageManage> list, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        JsonResult jsonResult = new JsonResult();
        /*if(files.length == 0){
            return jsonResult;
        }
        List<ImageManage> list = new ArrayList<ImageManage>();
        for(MultipartFile file: files){
       //     System.setProperty("sun.jnu.encoding","utf-8");
            String fileName = new String(file.getOriginalFilename().getBytes("utf8"), System.getProperty("sun.jnu.encoding"));
            File filePicture = new File(picturePath + fileName);
            if (!filePicture.getParentFile().exists()) {
                filePicture.getParentFile().mkdirs();
            }
            if(!filePicture.exists()){
                try {
                    filePicture.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return jsonResult;
                }
            }

            try {
                list.add(new ImageManage().setImgGroup(typeName).setName(file.getOriginalFilename()).setUrl(picturePrefix+file.getOriginalFilename()));
                file.transferTo(filePicture);
                imageManageService.insertImageManage(list);
            } catch (IOException e) {
                e.printStackTrace();
                return jsonResult;
            }
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);*/
        int result = imageManageService.insertImageManage(list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }
}
