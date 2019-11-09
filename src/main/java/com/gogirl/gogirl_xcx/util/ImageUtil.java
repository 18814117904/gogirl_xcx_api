package com.gogirl.gogirl_xcx.util;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class ImageUtil {
	public static String saveImage(String picturePath,MultipartFile file) throws IOException {
	   //     System.setProperty("sun.jnu.encoding","utf-8");
	        String fileName;
			fileName = new String(file.getOriginalFilename().replaceAll(",", ""));
//			fileName = new String(file.getOriginalFilename().replaceAll(",", "").getBytes("utf8"), System.getProperty("sun.jnu.encoding"));
	        File filePicture = new File(picturePath + fileName);
	        if (!filePicture.getParentFile().exists()) {
	            filePicture.getParentFile().mkdirs();
	        }
	        if(!filePicture.exists()){
	            filePicture.createNewFile();
	        }
	        file.transferTo(filePicture);
	        String qiniuName = qiniuUpload(filePicture);
//	    }
	    return qiniuName;
	}

	public  static String qiniuUpload(File localFilePath) {
	      //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
//      String accessKey = "MHRIqINwea8MnhFHXhARWofsKzF6WTqPzOp92VHy";
//      String secretKey = "TjZPPnshhkysa40VOjyzdCUJBn4E-QUD0kBoEQwk";
      String accessKey = "RWQXlbVA7oe3BxnPuFtqkAJocQZkWTwrwYyldklr";
      String secretKey = "tS2gxsQO26mGoFZJI-x8WSH9X5aPgYMJcyoJdak5";
      String bucket = "begogirls";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "E://test.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return null;
        }
				
	}
}
