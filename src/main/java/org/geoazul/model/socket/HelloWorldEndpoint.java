package org.geoazul.model.socket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
 
  
@ServerEndpoint("/hello")
public class HelloWorldEndpoint {
 
     
    @OnMessage
    public String hello(String message) {
        return message;
    }
 
    @OnOpen
    public void myOnOpen(Session session) {
    }
 
    @OnClose
    public void myOnClose(CloseReason reason) {
    }
 
}