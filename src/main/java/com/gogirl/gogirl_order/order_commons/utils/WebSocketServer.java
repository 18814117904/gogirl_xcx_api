package com.gogirl.gogirl_order.order_commons.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by yinyong on 2019/3/4.
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{departmentId}")
@Component
public class WebSocketServer {

    //记录当前在线人数
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的Websocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    // 唯一标识 店铺id
    private String departmentId;
    //与某个客户端的链接会话
    private Session session;

    /**
     * 链接建立成功调用的方法
     * @param departmentId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("departmentId") String departmentId, Session session) {
        this.departmentId = departmentId;
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
        	System.out.println("websocket IO异常");
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息："+ message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
    	System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送给指定用户
     * @param message
     * @param departmentId
     * @throws IOException
     */
    public static void sendMessageTo(String message, String departmentId) throws IOException {
        for(WebSocketServer item : webSocketSet) {
            if(item.departmentId.equals(departmentId)) {
                item.session.getBasicRemote().sendText(message);
            }
        }
    }

    public static void sendMessageByTimmer(String message, String departmentId) throws IOException {
        for(WebSocketServer item : webSocketSet) {
            if(item.departmentId.equals(departmentId)) {
                item.session.getBasicRemote().sendText(message);
            }else {
                Map<String, String> msgMap = new HashMap<String, String>();
                Map<String, String> raingMap = new HashMap<String, String>();
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                msgMap.put("type", "1");
                msgMap.put("number","0");

                raingMap.put("type", "2");
                raingMap.put("number", "0");
                list.add(msgMap);
                list.add(raingMap);
                item.session.getBasicRemote().sendText(JSONObject.toJSONString(list));
            }
        }
    }

    /**
     * 群发自定义消息
     * @param message
     */
    public static void sendInfo(String message) {
        for(WebSocketServer item : webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
