import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BoosterPage {

    TableView<Order> table;

    public void createBoosterPage(Stage window) {
        //Id column
        TableColumn<Order, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(200);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Price column
        TableColumn<Order, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Purchase column
        TableColumn<Order, String> purchaseColumn = new TableColumn<>("Purchase");
        purchaseColumn.setMinWidth(100);
        purchaseColumn.setCellValueFactory(new PropertyValueFactory<>("purchase"));

        //Comment column
        TableColumn<Order, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setMinWidth(100);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
<<<<<<< HEAD



=======
        
>>>>>>> e6dea4069bbbed4f7bcc4575e7c1c1398d0fd598
        table = new TableView<>();
        table.setItems(initData());
        table.getColumns().addAll(idColumn, priceColumn, purchaseColumn, commentColumn);

        Button launchButton = new Button("Launch");
        launchButton.setOnAction(e -> launchButtonClicked());
        HBox hBox = new HBox();
        hBox.getChildren().addAll(launchButton);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    public ObservableList<Order> initData(){
        ObservableList<Order> products = FXCollections.observableArrayList();
<<<<<<< HEAD
        products.add(new Order(1, 859.00, "gold to dia", "pls", "asdf", "password1"));
        products.add(new Order(2, 2.49, "silver to dia", "ne bazd el", "laci", "password2"));
        products.add(new Order(3, 99.00, "bronze to silver", "je", "Laci", "password3"));
        products.add(new Order(4, 19.99, "gold to master", "", "peti", "password4"));
        products.add(new Order(5, 1.49, "master to challenger", "ez main", "répa", "password5"));
=======
        products.add(new Order(1, 859.00, "gold to dia", "pls", "asd", "asd"));
        products.add(new Order(2, 2.49, "silver to dia", "ne bazd el", "asd", "asd"));
        products.add(new Order(3, 99.00, "bronze to silver", "je", "asd", "asd"));
        products.add(new Order(4, 19.99, "gold to master", "", "asd", "asd"));
        products.add(new Order(5, 1.49, "master to challenger", "ez main", "asd", "asd"));
>>>>>>> e6dea4069bbbed4f7bcc4575e7c1c1398d0fd598
        return products;
    }

    public void launchButtonClicked(){
        Order orderSelected;
        List<String> accountData = new ArrayList<>();

        orderSelected = table.getSelectionModel().getSelectedItem();

        String username = orderSelected.getUsername();
        String password = orderSelected.getPassword();

        accountData.add(username);
        accountData.add(password);
    }
}