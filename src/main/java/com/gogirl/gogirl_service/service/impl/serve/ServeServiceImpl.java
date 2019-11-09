package com.gogirl.gogirl_service.service.impl.serve;

import com.gogirl.gogirl_service.dao.serve.ServeMapper;
import com.gogirl.gogirl_service.entity.ProduceServe;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_service.service.service.serve.ServeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinyong on 2018/8/21.
 */
@Service
public class ServeServiceImpl implements ServeService {

    @Autowired
    private ServeMapper serveMapper;

    @Override
    public List<Serve> listServeForPage(Serve serve, int skipNumber, int newPageSize) {
        return serveMapper.listServeForPage(serve, skipNumber, newPageSize);
    }


    public List<Integer> listServeCount(Serve serve){
        return serveMapper.listServeCount(serve);
    }

    public List<Integer> getCountByGroup(Serve serve){
        return serveMapper.getCountByGroup(serve);
    }
    
    @Override
    public List<Serve> listServeAllForPage(Serve serve) {
        return serveMapper.listServeAllForPage(serve);
    }
    @Override
    public List<Serve> queryNewServePage(Serve serve) {
        return serveMapper.queryNewServePage(serve);
    }

    @Override
    public List<Serve> queryServeForAll(Serve serve) {
        return serveMapper.queryServeForAll(serve);
    }

    @Override
    public List<Serve> listServeByShopIdForPage(Integer id) {
        return serveMapper.listServeByShopIdForPage(id);
    }

    public List<Integer> listServeByTypeCount(String type, Integer departmentId) {
        return serveMapper.listServeByTypeCount(type, departmentId);
    }

    public List<Integer> listServeByTypeAndGroup(String type, Integer departmentId) {
        return serveMapper.listServeByTypeAndGroup(type, departmentId);
    }

    @Override
    public List<Serve> listServeByType(String type, Integer departmentId, Integer skipNumber, Integer newPageSize) {
        return serveMapper.listServeByType(type, departmentId, skipNumber, newPageSize);
    }

    @Override
    @Transactional
    public int deleteServe(Integer id) {
        int result = 1;
        // 删除服务-款式关联关系
        result = serveMapper.deleteServeProduce(id);
        // 删除店铺-服务关联关系
        result = serveMapper.deleteShopServeByServeId(id);
        // 删除服务
        result = serveMapper.deleteServe(id);
        return result;
    }

    @Override
    public Serve getServeForDetail(Integer id) {
        return serveMapper.getServeForDetail(id);
    }
    @Override
    public Serve getServeAndProduceForDetail(Integer id) {
        return serveMapper.getServeAndProduceForDetail(id);
    }

    @Override
    @Transactional
    public int insertServe(Serve serve) {
        int result = serveMapper.insertServe(serve);
        serveMapper.insertServeStore(serve.getId());
        List<ProduceServe> list = new ArrayList<ProduceServe>();
        String [] produceArr = StringUtils.commaDelimitedListToStringArray(serve.getProduces());
        for(String pro : produceArr){
            ProduceServe produceServe = new ProduceServe();
            produceServe.setServeId(serve.getId());
            produceServe.setProduceId(Integer.parseInt(pro));
            list.add(produceServe);
        }
        if(list != null && list.size() > 0){
            serveMapper.insertServeProduce(list);
        }
        return result;
    }

    @Override
    @Transactional
    public int updateServe(Serve serve) {
        if(serve.getStatus() != null){
            return serveMapper.updateServe(serve);  // 用于修改上下线状态
        }
        serveMapper.deleteServeProduce(serve.getId());
            List<ProduceServe> prolist = new ArrayList<ProduceServe>();
            String [] produceArr = StringUtils.commaDelimitedListToStringArray(serve.getProduces());
            for(String pro : produceArr){
                ProduceServe produceServe = new ProduceServe();
                produceServe.setProduceId(Integer.parseInt(pro));
                produceServe.setServeId(serve.getId());
                prolist.add(produceServe);
            }
            if(prolist.size() > 0){
                serveMapper.insertServeProduce(prolist);
            }
        return serveMapper.updateServe(serve);
    }
}