package org.geoazul.model.socket;

import java.io.IOException;
import java.security.Principal;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {

  @OnMessage	
  public void onMessage(String message, Session session) 
    throws IOException, InterruptedException {
  
	  Principal userPrincipal = session.getUserPrincipal();
	    if (userPrincipal == null) {
	    } else {
	        String user = userPrincipal.getName();
	        // now that your have your user here, your can do whatever other operation you want.
	    }
	  
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
	  
	  try {
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }

    
  
    // Send 3 messages to the client every 5 seconds
    int sentMessages = 0;
    while(sentMessages < 3){
      Thread.sleep(5000);
      session.getBasicRemote().
        sendText("This is an intermediate server message. Count: " 
          + sentMessages);
      sentMessages++;
    }
  
    // Send a final message to the client
    session.getBasicRemote().sendText("This is the last server message");
  }
  
  @OnOpen
  public void onOpen(Session session,  EndpointConfig config) {
	  
	  
	  
	  Principal userPrincipal = session.getUserPrincipal();
	    if (userPrincipal == null) {
	    } else {
	        String user = userPrincipal.getName();
	        // now that your have your user here, your can do whatever other operation you want.
	    }
	  
  }

  @OnClose
  public void onClose() {
  }
}