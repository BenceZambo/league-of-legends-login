package webService;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Order;
import model.Status;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;

import java.net.URISyntaxException;

public class WebSocketClient {
    io.socket.client.Socket socket;
    OrderService orderService;

    {
        try {
            socket = IO.socket("http://localhost");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    socket.emit("foo", "hi");
                    socket.disconnect();
                }

            }).on("orderNotification", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject)args[0];
                    String type = null;
                    try {
                        type = obj.getString("type");
                        int id = obj.getInt("id");
                        Order order = orderService.findByID(id);
                        switch (type){
                            case "pause":
                                order.setStatus(Status.PAUSED);
                                //TODO
                                break;
                            case "unpause":
                                order.setStatus(Status.PROCESSING);
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public WebSocketClient(OrderService orderService) {
        this.orderService = orderService;
    }
}
