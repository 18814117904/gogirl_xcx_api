package com.gogirl.gogirl_service.dao.serve;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gogirl.gogirl_service.entity.ProduceServe;
import com.gogirl.gogirl_service.entity.Serve;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.List;

/**
 * Created by yinyong on 2018/8/21.
 */
@Mapper
@Repository
public interface ServeMapper {

	List<Serve> listServeForPage(@Param("serve") Serve serve, @Param("skipNumber") int skipNumber, @Param("newPageSize") int newPageSize);

    List<Integer> listServeCount(Serve serve);

    List<Integer> getCountByGroup(Serve serve);

    List<Serve> listServeAllForPage(Serve serve);
    List<Serve> queryNewServePage(Serve serve);
    
    List<Serve> queryServeForAll(Serve serve);

    List<Serve> listServeByShopIdForPage(Integer id);

    List<Integer> listServeByTypeCount(@Param("type") String type, @Param("departmentId") Integer departmentId);

    List<Integer> listServeByTypeAndGroup(@Param("type") String type, @Param("departmentId") Integer departmentId);

    List<Serve> listServeByType(@Param("type") String type, @Param("departmentId") Integer departmentId, @Param("skipNumber") Integer skipNumber, @Param("newPageSize") Integer newPageSize);

    int deleteServe(Integer id);

    Serve getServeForDetail(Integer id);
    Serve getServeAndProduceForDetail(Integer id);
    int insertServe(Serve serve);

    int insertServeStore(Integer serveId);

    int updateServe(Serve serve);

    int insertServeProduce(List<ProduceServe> prolist);

    int deleteShopServeByServeId(Integer id);

    int deleteServeProduce(Integer id);

	String getServeShortCode(Integer serveId);
}
