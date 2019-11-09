package com.gogirl.gogirl_service.dao.broadcast;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.Broadcast;

import java.util.List;

/**
 * Created by yinyong on 2018/9/25.
 */
@Mapper
@Repository
public interface BroadcastMapper {

    List<Broadcast> listBroadcastForPage(Broadcast broadcast);

    List<Broadcast> listBroadcast();

    Broadcast getBroadcastForDetail(Integer id);

    int updateBroadcast(Broadcast broadcast);

    int deleteBreadcast(Integer id);

    int insertBroadcast(Broadcast broadcast);

    int topTimeBroadcast(Integer id);
}
