package com.gogirl.gogirl_service.service.service.image;

import com.gogirl.gogirl_service.entity.ImageManage;

import java.util.List;

/**
 * Created by yinyong on 2018/9/11.
 */
public interface ImageManageService {

    List<ImageManage> listImageManageForPage(ImageManage imageManage);

    int insertImageManage(List<ImageManage> listimage);

}
