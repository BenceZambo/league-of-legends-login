package webService;

import controller.BoosterPageController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import logger.Globals;
import model.Order;
import model.Status;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import services.WindowWatcher;
import view.AlertBox;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient {
    io.socket.client.Socket socket;
    OrderService orderService;

    BoosterPageController boosterPageController;

    public void joinServer(URI address) {
        {
            try {
                socket = IO.socket(address);
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        socket.emit("", "");
                        socket.disconnect();
                    }

                }).on("orderNotification", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        JSONObject obj = (JSONObject) args[0];
                        String type;
                        try {
                            type = obj.getString("type");
                            int id = obj.getInt("id");
                            Order order = orderService.findByID(id);
                            boosterPageController.initData();
                            switch (type) {
                                case "pause":
                                    //WindowWatcher windowWatcher = new WindowWatcher(Globals.lolGame);
                                    AlertBox.display("Your order paused", "Your order (" + id + ") has benn paused");
                                    break;
                                case "unpause":
                                    AlertBox.display("Your order is processing now", "Your order (" + id + ") is processing now");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                    }

                });
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public WebSocketClient(OrderService orderService, URI address) {
        this.orderService = orderService;
        joinServer(address);
    }

    public void send(String key, Object object){
        socket.emit(key, object);
    }

    public void disconnect(){
        socket.disconnect();
    }

    public void setBoosterPageController(BoosterPageController boosterPageController) {
        this.boosterPageController = boosterPageController;
    }
}
