package com.gogirl.gogirl_service.dao.image;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.ImageGroup;

import java.util.List;

/**
 * Created by yinyong on 2018/9/11.
 */
@Mapper
@Repository
public interface ImageGroupMapper {

    List<ImageGroup> listImageGroup();
}
