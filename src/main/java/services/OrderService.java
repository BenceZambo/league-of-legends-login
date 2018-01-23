package services;

import model.orders.Order;

public class OrderService {

    private Order[] arrayOfOrders;

    public OrderService() {
    }

    public Order[] getArrayOfOrders() {
        return arrayOfOrders;
    }

    public void setArrayOfOrders(Order[] arrayOfOrders) {
        this.arrayOfOrders = arrayOfOrders;
    }

    public Order findByID (int id){
        for (int i = 0; i < arrayOfOrders.length; i++){
            if (arrayOfOrders[i].getId() == id){
                return arrayOfOrders[i];
            }
        }
        // return null if the order with that id is not exist
        return null;
    }
}
