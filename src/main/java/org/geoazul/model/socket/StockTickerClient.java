package org.geoazul.model.socket;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.Session;

@ClientEndpoint
public class StockTickerClient {

    @OnClose
    public void closed(Session session) {

    }

    @OnError
    public void error(Throwable error) {

    }

}
