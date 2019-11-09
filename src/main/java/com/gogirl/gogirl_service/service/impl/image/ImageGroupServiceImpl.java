package com.gogirl.gogirl_service.service.impl.image;

import com.gogirl.gogirl_service.dao.image.ImageGroupMapper;
import com.gogirl.gogirl_service.entity.ImageGroup;
import com.gogirl.gogirl_service.service.service.image.ImageGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2018/9/11.
 */
@Service
public class ImageGroupServiceImpl implements ImageGroupService {

    @Autowired
    private ImageGroupMapper imageGroupMapper;

    @Override
    public List<ImageGroup> listImageGroup() {
        return imageGroupMapper.listImageGroup();
    }
}
