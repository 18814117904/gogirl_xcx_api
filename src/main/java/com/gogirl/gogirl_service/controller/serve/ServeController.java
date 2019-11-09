package com.gogirl.gogirl_service.controller.serve;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.dao.PraiseRecordMapper;
import com.gogirl.gogirl_service.entity.PraiseRecord;
import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_service.service.service.produce.ProduceService;
import com.gogirl.gogirl_service.service.service.serve.ServeService;
import com.gogirl.gogirl_service.service.service.type.TypeService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping( "serve")
@Api(tags = { "5.服务管理" },value = "服务管理")
public class ServeController {
    @Resource
    private ServeService serveService;
    @Resource
    private ProduceService produceService;
    @Resource
    private GogirlTokenService tokenService;
    @Resource
    private PraiseRecordMapper praiseRecordMapper;
    @Resource
    TypeService typeService;
    @RequestMapping(method={RequestMethod.GET},value = "queryNewServePage")
	@ApiOperation(value = "1.新品首发列表", notes = "新品首发列表，如果有")
    public JsonResult<List<Serve>> queryNewServePage(String token,@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){
        if(pageNum != null && pageSize != null){
            PageHelper.startPage(pageNum,pageSize);
        }
        Serve serve = new Serve();
        serve.setHasIndexShow(1);
        GogirlToken gogirlToken = tokenService.getTokenByToken(token);
        if(gogirlToken!=null&&gogirlToken.getCustomerId()!=null){
        	serve.setCusId(gogirlToken.getCustomerId());
        }
//      List<Serve> lists = serveService.queryNewServePage(serve);
        List<Serve> lists = new ArrayList<Serve>();
        List<Produce> listsProduce = produceService.queryNewServePage(gogirlToken.getCustomerId());
        Iterator<Produce> it = listsProduce.iterator();
        while(it.hasNext()){
        	Produce item = it.next();
        	Serve itemServe = item.getServe();
        	item.setServe(null);
        	itemServe.setProduce(item);
        	lists.add(itemServe);
        }
        PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
        return new JsonResult(true,JsonResult.APP_DEFINE_SUC,pageInfo);
    }
    /*服务点赞*/
    @RequestMapping(method={RequestMethod.POST},value = "praiseServe")
	@ApiOperation(value = "2.小程序首页服务,点赞和取消点赞", notes = "小程序首页服务点赞")
    public JsonResult<Object> praiseServe(String token,Integer serveId){
    	GogirlToken t = tokenService.getTokenByToken(token);
    	if(t==null){
    		return new JsonResult<Object>(false,JsonResult.APP_TOKEN_INVALID,null);
    	}
    	if(serveId==null){
    		return new JsonResult<Object>(false,"serveId为空",null);
    	}
    	Serve queServe = serveService.getServeForDetail(serveId);
    	if(queServe==null){
    		return new JsonResult<Object>(false,"无该服务serveId:"+serveId,null);
    	}
    	Integer praiseSum = queServe.getPraiseSum();
    	if(praiseSum==null){
    		praiseSum=0;
    	}
    	
    	//查看用户有没有点赞过，如果有，取消点赞，如果没有，点赞
    	PraiseRecord parampraise = new PraiseRecord();
    	parampraise.setCustomerId(t.getCustomerId());
    	parampraise.setServeId(serveId);
    	PraiseRecord praiseRecord = praiseRecordMapper.selectByCustomerIdAndServ(parampraise);
    	
    	if(praiseRecord==null){
        	Serve serve = new Serve();
        	serve.setId(serveId);
        	serve.setPraiseSum(++praiseSum);
        	serveService.updateServe(serve);

        	PraiseRecord updatepraise = new PraiseRecord();
        	updatepraise.setCustomerId(t.getCustomerId());
        	updatepraise.setServeId(serveId);
        	updatepraise.setTime(new Date());
        	praiseRecordMapper.insertSelective(updatepraise);
            return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC+"-点赞",null) ;
    	}else{
        	Serve serve = new Serve();
        	serve.setId(serveId);
        	serve.setPraiseSum(--praiseSum);
        	serveService.updateServe(serve);

        	praiseRecordMapper.deleteByPrimaryKey(praiseRecord.getId());
            return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC+"-取消点赞",null) ;
    	}
    }
    /*款式点赞*/
    /*款式点赞*/
    @RequestMapping(method={RequestMethod.POST},value = "praiseProduce")
	@ApiOperation(value = "2.小程序首页款式,点赞和取消点赞", notes = "小程序首页款式点赞")
    public JsonResult<Object> praiseProduce(String token,Integer produceId){
    	GogirlToken t = tokenService.getTokenByToken(token);
    	if(t==null){
    		return new JsonResult<Object>(false,JsonResult.APP_TOKEN_INVALID,null);
    	}
    	if(produceId==null){
    		return new JsonResult<Object>(false,"serveId为空",null);
    	}
    	Produce produce = produceService.getProduceForDetail(produceId);
    	if(produce==null){
    		return new JsonResult<Object>(false,"无该款式produceId:"+produceId,null);
    	}
    	Integer praiseSum = produce.getPraiseSum();
    	if(praiseSum==null){
    		praiseSum=0;
    	}
    	
    	//查看用户有没有点赞过，如果有，取消点赞，如果没有，点赞
    	PraiseRecord parampraise = new PraiseRecord();
    	parampraise.setCustomerId(t.getCustomerId());
    	parampraise.setServeId(produceId);
    	PraiseRecord praiseRecord = praiseRecordMapper.selectByCustomerIdAndServ(parampraise);
    	if(praiseRecord==null){
    		Produce updateproduce = new Produce();
    		updateproduce.setId(produceId);
    		updateproduce.setPraiseSum(++praiseSum);
    		produceService.updateProduceById(updateproduce);

        	PraiseRecord updatepraise = new PraiseRecord();
        	updatepraise.setCustomerId(t.getCustomerId());
        	updatepraise.setServeId(produceId);
        	updatepraise.setTime(new Date());
        	updatepraise.setType(2);//1.服务;2.款式type
        	praiseRecordMapper.insertSelective(updatepraise);
            return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC+"-点赞",null) ;
    	}else{
    		Produce updateproduce = new Produce();
    		updateproduce.setId(produceId);
    		updateproduce.setPraiseSum(--praiseSum);
    		produceService.updateProduceById(updateproduce);
        	praiseRecordMapper.deleteByPrimaryKey(praiseRecord.getId());
            return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC+"-取消点赞",null) ;
    	}
    }
    
    @ApiOperation(value = "查看服务详情", notes = "")
    @RequestMapping(method={RequestMethod.GET},value = "getServeDetail")
    public JsonResult<Serve> getServeDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult<Serve> jsonResult = new JsonResult<Serve>();
        Serve serve = serveService.getServeForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(serve);
        return jsonResult;
    }
    
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeForDetail")
    public JsonResult<Serve> queryServeDetail(Integer id, String type, HttpServletRequest request, HttpServletResponse response){
        JsonResult<Serve> jsonResult = new JsonResult<Serve>();
        Serve serve = serveService.getServeForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(serve);
        return jsonResult;
    }
    
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeForPage")
    public JsonResult<PageInfo<Serve>> queryServeForPage(Serve serve, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize, HttpServletRequest request, HttpServletResponse response){
        JsonResult<PageInfo<Serve>> jsonResult = new JsonResult<PageInfo<Serve>>();

         //查询所有符合条件服务数量
         List<Integer> listServeCount = serveService.listServeCount(serve);

         int totalServeCount = (listServeCount.size() + (pageSize - 1))/pageSize;

         //查询每个订单下款式数量
         List<Integer> getCountByGroup = serveService.getCountByGroup(serve);

         //重新获取页码
         int skipNumber = getCountByGroup.subList(0, 
        		 (pageNum -1) * pageSize>getCountByGroup.size()?getCountByGroup.size():(pageNum -1) * pageSize
        				 ).stream().mapToInt(Integer::intValue).sum();

         int newPageSize= getCountByGroup.subList(
        		 (pageNum -1) * pageSize, 
        		 pageNum *pageSize>getCountByGroup.size()?getCountByGroup.size():pageNum*pageSize
        				 ).stream().mapToInt(Integer::intValue).sum();

         /*if(pageNum != null && pageSize != null){
         PageHelper.startPage(skipNumber,newPageSize);
         }*/

         List<Serve> lists = serveService.listServeForPage(serve, skipNumber, newPageSize);
         PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
         pageInfo.setPageNum(pageNum);
         pageInfo.setPageSize(pageSize);
         pageInfo.setTotal(listServeCount.size());
         pageInfo.setPages(totalServeCount);
         jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    
    
//    @ApiIgnore()
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeForPageXcx")
//    public JsonResult<PageInfo<Serve>> queryServeForPageXcx(Serve serve, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize, HttpServletRequest request, HttpServletResponse response){
//        JsonResult<PageInfo<Serve>> jsonResult = new JsonResult<PageInfo<Serve>>();
//        List<Type> typeList = typeService.listType();
//        Map<String, Integer> typeMap = new HashMap<>();
//        Iterator<Type> it = typeList.iterator();
//        while(it.hasNext()){
//        	Type item = it.next();
//        	typeMap.put(item.getName(), item.getId());
//        }
//        PageHelper.startPage(pageNum,pageSize);
//        if(serve.getType()!=null){
//            serve.setTypeId(typeMap.get(serve.getType()));
//            serve.setType(null);
//        }
//        List<Serve> lists = serveService.listServeForPageXcx(serve);
//        PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
//         jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
//        return jsonResult;
//    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeAllForPage")
    public JsonResult<PageInfo<Serve>> queryServeAllForPage(Serve serve, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize, HttpServletRequest request, HttpServletResponse response){
        JsonResult<PageInfo<Serve>> jsonResult = new JsonResult<PageInfo<Serve>>();
        if(pageNum != null && pageSize != null){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<Serve> lists = serveService.listServeAllForPage(serve);
        PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeForAll")
    public JsonResult<List<Serve>> queryServe(Serve serve, HttpServletRequest request, HttpServletResponse response){
        JsonResult<List<Serve>> jsonResult = new JsonResult<List<Serve>>();
        List<Serve> list = serveService.queryServeForAll(serve);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeByShopIdForPage")
    public JsonResult<PageInfo<Serve>> queryServeByShopIdForPage(Integer shopId, HttpServletRequest request, HttpServletResponse response){
        JsonResult<PageInfo<Serve>> jsonResult = new JsonResult<PageInfo<Serve>>();
        /*String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));*/
        Map<String, Object> map = new HashMap<String, Object>();
        List<Serve> lists = serveService.listServeByShopIdForPage(shopId);
        PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
        pageInfo.setPages(1);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryServeByType")
    public JsonResult<?> queryServeByType(String type, Integer departmentId, Integer pageNum, Integer pageSize, HttpServletRequest request, HttpServletResponse response){
        JsonResult<Object> jsonResult = new JsonResult<Object>();

        //查询所有符合条件服务数量
        List<Integer> listServeCount = serveService.listServeByTypeCount(type, departmentId);

        if(pageNum == null || pageSize == null) {
            List<Serve> lists = serveService.listServeByType(type, departmentId, pageNum, pageSize);
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
            return jsonResult;
        }

        if(listServeCount == null) {
            PageInfo<Serve> pageInfo = new PageInfo<Serve>();
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
            return jsonResult;
        }

        int totalServeCount = (listServeCount.size() + (pageSize - 1))/pageSize;

        //查询每个订单下款式数量
        List<Integer> getCountByGroup = serveService.listServeByTypeAndGroup(type, departmentId);

        //重新获取页码
        int skipNumber = getCountByGroup.subList(0, (pageNum -1) * pageSize>getCountByGroup.size()?getCountByGroup.size():(pageNum -1) * pageSize).stream().mapToInt(Integer::intValue).sum();

        /*if(skipNumber > 0) {
            skipNumber = skipNumber + 1;
        }*/

        int newPageSize= getCountByGroup.subList((pageNum -1) * pageSize, pageNum *pageSize>getCountByGroup.size()?getCountByGroup.size():pageNum*pageSize).stream().mapToInt(Integer::intValue).sum();

        /*if(pageNum != null && pageSize != null){
            PageHelper.startPage(skipNumber,newPageSize);
        }*/
        List<Serve> lists = serveService.listServeByType(type, departmentId, skipNumber, newPageSize);
        PageInfo<Serve> pageInfo = new PageInfo<Serve>(lists);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(listServeCount.size());
        pageInfo.setPages(totalServeCount);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteServe")
    public JsonResult<?> deleteServe(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = serveService.deleteServe(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
    @ApiIgnore()
    @PostMapping(value = "addServe")
    public JsonResult<?> addServe(Serve serve, HttpServletRequest request, HttpServletResponse response){
        int result = serveService.insertServe(serve);
        JsonResult<?> jsonResult = new JsonResult<Object>();
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyServe")
    public JsonResult<?> modifyServe(Serve serve, HttpServletRequest request, HttpServletResponse response){
        JsonResult<?> jsonResult = new JsonResult<Object>();
        int result = serveService.updateServe(serve);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

}
