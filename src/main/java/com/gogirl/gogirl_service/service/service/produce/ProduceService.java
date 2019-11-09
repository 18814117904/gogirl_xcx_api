package com.gogirl.gogirl_service.service.service.produce;

import com.gogirl.gogirl_service.entity.Produce;

import java.util.List;

/**
 * Created by yinyong on 2018/8/23.
 */
public interface ProduceService {

    List<Produce> listProduceForPage(Produce produce);

    List<Produce> queryProduceWithPrice();

    int deleteProdecu(Integer id);

    int insertProduce(Produce produce);

    Produce getProduceForDetail(Integer id);

    int updateProduceById(Produce produce);

    List<Produce> listProduceAll();

    List<Produce> listProduceByServe(Integer serveId);

    List<Produce> listProduceNoDesignationServe(Integer serveId);

    List<Produce> listProduceNoDesignationAllServe();

    int topProduce(Integer id);

	List<Produce> queryNewServePage(Integer cusId);
}
