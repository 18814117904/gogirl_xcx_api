package com.gogirl.gogirl_service.service.impl.produce;

import com.gogirl.gogirl_service.dao.produce.ProduceMapper;
import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.entity.ProduceServe;
import com.gogirl.gogirl_service.service.service.produce.ProduceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

/**
 * Created by yinyong on 2018/8/23.
 */
@Service
public class ProduceServiceImpl implements ProduceService {

    @Autowired
    private ProduceMapper produceMapper;

    @Override
    public List<Produce> listProduceForPage(Produce produce) {
        return produceMapper.listProduceForPage(produce);
    }

    @Override
    public List<Produce> queryProduceWithPrice() {
        return produceMapper.queryProduceWithPrice();
    }

    @Override
    public int deleteProdecu(Integer id) {
        int result = 0;
        result = produceMapper.deleteProduceServe(id);
        result = produceMapper.deleteProdecu(id);
        return result;
    }

    @Override
    @Transactional
    public int insertProduce(Produce produce) {
        int result = produceMapper.insertProduce(produce);
        if(produce.getServeId() != null){
            produceMapper.insertProduceServe(produce.getServeId(), produce.getId());
        }
        return result;
    }

    @Override
    public Produce getProduceForDetail(Integer id) {
        return produceMapper.getProduceForDetail(id);
    }

    @Override
    public int updateProduceById(Produce produce) {
        if(produce.getStatus() != null){
            return produceMapper.updateProduceById(produce); // 用于修改上下线状态
        }
        ProduceServe produceServe =  produceMapper.checkProduceServe(produce.getId());
        if(produceServe != null){
            produceMapper.updateProduceServe(produce); //修改服务款式关联关系
        }else{
            produceMapper.insertProduceServe(produce.getServeId(), produce.getId()); //新增服务款式关联关系
        }
        return produceMapper.updateProduceById(produce);
    }

    @Override
    public List<Produce> listProduceAll() {
        return produceMapper.listProduceAll();
    }

    @Override
    public List<Produce> listProduceByServe(Integer serveId) {
        return produceMapper.listProduceByServe(serveId);
    }

    @Override
    public List<Produce> listProduceNoDesignationServe(Integer serveId) {
        return produceMapper.listProduceNoDesignationServe(serveId);
    }

    @Override
    public List<Produce> listProduceNoDesignationAllServe() {
        return produceMapper.listProduceNoDesignationAllServe();
    }

    @Override
    public int topProduce(Integer id) {
        return produceMapper.topProduce(id);
    }

	@Override
	public List<Produce> queryNewServePage(Integer cusId) {
		return produceMapper.queryNewServePage(cusId);
	}
}
