package com.gogirl.gogirl_service.service.impl.image;

import com.gogirl.gogirl_service.dao.image.ImageManageMapper;
import com.gogirl.gogirl_service.entity.ImageManage;
import com.gogirl.gogirl_service.service.service.image.ImageManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2018/9/11.
 */
@Service
public class ImageManageServiceImpl implements ImageManageService {

    @Autowired
    private ImageManageMapper imageManageMapper;

    @Override
    public List<ImageManage> listImageManageForPage(ImageManage imageManage) {
        return imageManageMapper.listImageManageForPage(imageManage);
    }

    @Override
    public int insertImageManage(List<ImageManage> listimage) {
        return imageManageMapper.insertImageManage(listimage);
    }
}
