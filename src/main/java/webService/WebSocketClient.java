package webService;

import controller.BoosterPageController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.orders.Order;
import org.json.JSONException;
import org.json.JSONObject;
import services.WindowWatcher;

import java.net.URI;

public class WebSocketClient {
    io.socket.client.Socket socket;

    BoosterPageController boosterPageController;


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
                            String type = obj.getString("type");
                            int id = obj.getInt("id");
                            Order order = boosterPageController.getCurrentOrder();
                            if (order.getId().equals(15832) && type.equals("pause")) {
                                System.out.println("WARNING");
                                boosterPageController.initData();
                                Thread thread = new Thread(new WindowWatcher());
                                thread.start();
                            }
                            if (type == "unpause") {
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
}
