package webService;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Order;
import model.Status;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import view.AlertBox;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient {
    io.socket.client.Socket socket;
    OrderService orderService;
    public void joinServer(URI address) {
        {
            try {
                socket = IO.socket(address);
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        socket.emit("", "hi");
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
                            switch (type) {
                                case "pause":
                                    order.setStatus(Status.PAUSED);
                                    AlertBox.display("Your order paused", "Your order paused");
                                    //TODO
                                    break;
                                case "unpause":
                                    order.setStatus(Status.PROCESSING);
                                    AlertBox.display("Your order is processing now", "Your order is processing now");
                                    //TODO
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
}
