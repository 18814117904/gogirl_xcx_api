package com.gogirl.gogirl_service.dao.image;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.ImageManage;

import java.util.List;

/**
 * Created by yinyong on 2018/9/11.
 */
@Mapper
@Repository
public interface ImageManageMapper {

    List<ImageManage> listImageManageForPage(ImageManage imageManage);

    int insertImageManage(List<ImageManage> listimage);
}
