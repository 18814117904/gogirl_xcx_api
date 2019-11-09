package com.gogirl.gogirl_service.service.service.serve;

import com.gogirl.gogirl_service.entity.Serve;

import java.util.List;

/**
 * Created by yinyong on 2018/8/21.
 */
public interface ServeService {

    List<Serve> listServeForPage(Serve serve, int skipNumber, int newPageSize);

    List<Integer> listServeCount(Serve serve);

    List<Integer> getCountByGroup(Serve serve);

    List<Serve> listServeAllForPage(Serve serve);

    List<Serve> queryServeForAll(Serve serve);

    List<Serve> listServeByShopIdForPage(Integer id);

    List<Integer> listServeByTypeCount(String type, Integer departmentId);

    List<Integer> listServeByTypeAndGroup(String type, Integer departmentId);

    List<Serve> listServeByType(String type, Integer departmentId, Integer skipNumber, Integer newPageSize);

    int deleteServe(Integer id);

    Serve getServeForDetail(Integer id);

    int insertServe(Serve serve);

    int updateServe(Serve serve);

	List<Serve> queryNewServePage(Serve serve);

	Serve getServeAndProduceForDetail(Integer id);
}
