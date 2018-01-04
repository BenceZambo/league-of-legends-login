package webService;

import controller.BoosterPageController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Order;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import view.AlertBox;

import java.net.URI;

public class WebSocketClient {
    io.socket.client.Socket socket;

    OrderService orderService;

    BoosterPageController boosterPageController;

    User user;

    public void joinServer(URI address) {
            try {
                socket = IO.socket(address);
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        System.out.println("connected");
                    }

                }).on("orderNotification", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        JSONObject obj = (JSONObject) args[0];
                        try {
                            Integer[] boosterID =(Integer[]) obj.get("to");
                            if (boosterID[0] == user.getId()) {
                                String type = obj.getString("type");
                                int id = obj.getInt("id");
                                Order order = boosterPageController.getCurrentOrder();
                                if (order.getId() == id && type == "pause") {
                                    AlertBox.display("WARNING", "Order has been paused, please sign out!");
                                }
                                boosterPageController.initData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        System.out.println("disconnect");
                    }

                }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                System.out.println("Failed to connect");
                            }
                        });
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public WebSocketClient(URI address) {
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


    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
