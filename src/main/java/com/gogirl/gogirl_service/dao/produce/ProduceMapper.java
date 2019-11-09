package com.gogirl.gogirl_service.dao.produce;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.entity.ProduceServe;

import java.util.List;

/**
 * Created by yinyong on 2018/8/23.
 */
@Mapper
@Repository
public interface ProduceMapper {

    List<Produce> listProduceForPage(Produce produce);

    List<Produce> queryProduceWithPrice();

    int deleteProdecu(Integer id);

    int deleteProduceServe(Integer id);

    int insertProduce(Produce produce);

    int insertProduceServe(@Param("serveId") Integer serveId, @Param("id") Integer id);

    Produce getProduceForDetail(Integer id);

    ProduceServe checkProduceServe(Integer id);

    int updateProduceServe(Produce produce);

    int updateProduceById(Produce produce);

    List<Produce> listProduceAll();

    List<Produce> listProduceByServe(Integer serveId);

    List<Produce> listProduceNoDesignationServe(Integer serveId);

    List<Produce> listProduceNoDesignationAllServe();

    int topProduce(Integer id);

	List<Produce> queryNewServePage(Integer cusId);
}
