package com.gogirl.gogirl_service.service.service.brodacast;

import com.gogirl.gogirl_service.entity.Broadcast;

import java.util.List;

/**
 * Created by yinyong on 2018/9/25.
 */
public interface BroadcastService {

    List<Broadcast> listBroadcastForPage(Broadcast broadcast);

    List<Broadcast> listBroadcast();

    Broadcast getBroadcastForDetail(Integer id);

    int updateBroadcast(Broadcast broadcast);

    int deleteBreadcast(Integer id);

    int insertBroadcast(Broadcast broadcast);

    int topTimeBroadcast(Integer id);
}
