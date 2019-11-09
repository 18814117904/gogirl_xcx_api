package com.gogirl.gogirl_service.service.impl.broadcast;

import com.gogirl.gogirl_service.dao.broadcast.BroadcastMapper;
import com.gogirl.gogirl_service.entity.Broadcast;
import com.gogirl.gogirl_service.service.service.brodacast.BroadcastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2018/9/25.
 */
@Service
public class BroadcastServiceImpl implements BroadcastService {

    @Autowired
    private BroadcastMapper broadcastMapper;

    @Override
    public List<Broadcast> listBroadcastForPage(Broadcast broadcast) {
        return broadcastMapper.listBroadcastForPage(broadcast);
    }

    @Override
    public List<Broadcast> listBroadcast() {
        return broadcastMapper.listBroadcast();
    }

    @Override
    public Broadcast getBroadcastForDetail(Integer id) {
        return broadcastMapper.getBroadcastForDetail(id);
    }

    @Override
    public int updateBroadcast(Broadcast broadcast) {
        return broadcastMapper.updateBroadcast(broadcast);
    }

    @Override
    public int deleteBreadcast(Integer id) {
        return broadcastMapper.deleteBreadcast(id);
    }

    @Override
    public int insertBroadcast(Broadcast broadcast) {
        return broadcastMapper.insertBroadcast(broadcast);
    }

    @Override
    public int topTimeBroadcast(Integer id) {
        return broadcastMapper.topTimeBroadcast(id);
    }
}
