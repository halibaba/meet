package com.meet.websocket.two.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: workdata-message
 * @ClassName WebSocketHandler
 * @description: WebSocket执行器
 * @author: MT
 * @create: 2022-12-16 15:37
 **/
@Component
@ServerEndpoint("/websocket/{terminalId}")
public class WorkDateSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(WorkDateSocketHandler.class);

	/**
	 * 以通道名称为key，连接会话为对象保存起来
	 */
	public static Map<String, Session> websocketClients = new ConcurrentHashMap<String, Session>();
	/**
	 * 会话
	 */
	private Session session;
	/**
	 * 通道名称
	 */
	private String socketname;
	/**
	 * 保存连接信息
	 */
	private static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();
	@OnOpen
	public void onOpen(@PathParam("terminalId") String terminalId, Session session) throws Exception {
		this.socketname = terminalId;
		this.session = session;
		if(websocketClients.get(socketname)==null){
			websocketClients.put(socketname, session);
			System.out.println("当前socket通道"+socketname+"已加入连接");
		}
	}
	@OnClose
	public void onClose(@PathParam("terminalId") String terminalId, Session session) throws Exception {
		websocketClients.remove(socketname);
		System.out.println("当前socket通道"+socketname+"已退出连接");
	}
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("当前收到了消息："+message);
		session.getAsyncRemote().sendText("来自服务器："+this.socketname+"你的消息我收到啦");
	}
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error(error.toString());
	}
	public void onClose(Session session) {
		try {
			session.close();
		} catch (IOException e) {
			logger.error("金斗云关闭连接异常：" + e);
		}
	}
	public void sendMessage(String message, Session nowsession) {
		if(nowsession!=null){
			try {
				nowsession.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void sendMessage(String terminalId, String message) {
		Session nowsession = websocketClients.get(terminalId);
		if(nowsession!=null){
			try {
				nowsession.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}