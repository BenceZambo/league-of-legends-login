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

import static com.sun.org.apache.xml.internal.serializer.Version.getProduct;
import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

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

        table = new TableView<>();
        table.setItems(getOrders());
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

    public ObservableList<Order> getOrders(){
        ObservableList<Order> products = FXCollections.observableArrayList();
        products.add(new Order(1, 859.00, "gold to dia", "pls", "asd", "asd"));
        products.add(new Order(2, 2.49, "silver to dia", "ne bazd el", "asd", "asd"));
        products.add(new Order(3, 99.00, "bronze to silver", "je", "asd", "asd"));
        products.add(new Order(4, 19.99, "gold to master", "", "asd", "asd"));
        products.add(new Order(5, 1.49, "master to challenger", "ez main", "asd", "asd"));
        return products;
    }

    public void launchButtonClicked(){
        System.out.println("Launch lol.exe");
    }
}
