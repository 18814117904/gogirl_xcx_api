package com.gogirl.gogirl_store.store_shop.service.impl;

import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_store.store_commons.utils.RandomUtils;
import com.gogirl.gogirl_store.store_shop.dao.ShopManageMapper;
import com.gogirl.gogirl_store.store_shop.service.ShopManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by yinyong on 2018/9/17.
 */
@Service
public class ShopManageServiceImpl implements ShopManageService {

    @Autowired
    private ShopManageMapper shopManageMapper;

    @Override
    public List<ShopManage> listShopManageForPage(ShopManage shopManage) {
        return shopManageMapper.listShopManageForPage(shopManage);
    }

    @Override
    public List<ShopManage> listShopByServeIdForPage(Integer id) {
        return shopManageMapper.listShopByServeIdForPage(id);
    }

    @Override
    public ShopManage getShopManageForDetail(Integer id) {
        return shopManageMapper.getShopManageForDetail(id);
    }

    @Override
    @Transactional
    public int updateShopManage(ShopManage shopManage) {
        return shopManageMapper.updateShopManage(shopManage);
    }

    @Override
    @Transactional
    public int deleteShopManageById(Integer id) {
        int result = 0;
             // 删除店铺-服务关联关系
              result = shopManageMapper.deleteShopServeByShopId(id);
              // 删除店铺-美甲师关联关系
              result = shopManageMapper.deleteShopTechnicianById(id);
              // 删除店铺
              result = shopManageMapper.deleteShopManageById(id);
        return result;
    }

    @Override
    @Transactional
    public int insertShopManage(ShopManage shopManage) {
        shopManage.setStoreNo(createShopNO());
        int result = shopManageMapper.insertShopManage(shopManage);
        shopManageMapper.insertShopServe(shopManage.getId());
        return result;
    }

    public String createShopNO(){
        String sole = RandomUtils.random("STO");
        ShopManage shopManage = shopManageMapper.checkShopManageNo(sole);
        while(null != shopManage && null != shopManage.getId()){
            sole = RandomUtils.random("M");
            shopManage = shopManageMapper.checkShopManageNo(sole);
        }
        return sole;
    }
}
