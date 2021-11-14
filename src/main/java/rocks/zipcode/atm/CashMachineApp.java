package rocks.zipcode.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField idField = new TextField();
    private TextField depositField = new TextField();
    private TextField withdrawField = new TextField();
    private Text sceneTitle = new Text("Welcome to ZipCodeBank's ATM.");
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Alert loginWarning = new Alert(Alert.AlertType.WARNING);
    private TextArea idTextArea = new TextArea();

    private GridPane grid = new GridPane();

    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 300);
        idField.setPromptText("Enter ID number.");

        TextArea areaInfo = new TextArea();

        Text idText = new Text();
        idTextArea.setPrefHeight(0);
        //FlowPane idPane = new FlowPane(20, 0, idText, idTextArea);

        //GridPane for Form layout
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label loginID = new Label("Login ID: ");
        Label balance = new Label("Balance: ");
        grid.add(loginID, 0, 1);
        grid.add(balance, 0, 2);


        //Button Creation - Login, Deposit, Withdraw, Exit
        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(e -> {
            //Warning
            if (idField.getText().equals("")) {
                loginWarning.showAndWait();
            }
            int id = Integer.parseInt(idField.getText());
            cashMachine.login(id);

            areaInfo.setText(cashMachine.toString());
        });

        Button btnDeposit = new Button("Deposit");
        btnDeposit.setOnAction(e -> {
            Float amount = Float.parseFloat(depositField.getText());
            cashMachine.deposit(amount);

            areaInfo.setText(cashMachine.toString());
        });

        Button btnWithdraw = new Button("Withdraw");
        btnWithdraw.setOnAction(e -> {
            Float amount = Float.parseFloat(withdrawField.getText());
            cashMachine.withdraw(amount);

            areaInfo.setText(cashMachine.toString());
        });

        Button btnExit = new Button("Logout");
        btnExit.setOnAction(e -> {
            cashMachine.exit();

            areaInfo.setText(cashMachine.toString());
        });

        //Disable Buttons
        btnDeposit.setDisable(true);
        btnWithdraw.setDisable(true);

        //FlowPanes - id, deposit, withdraw
        FlowPane flowpane = new FlowPane();

        FlowPane idFlowPane = new FlowPane();
        idFlowPane.getChildren().add(idField);
        idFlowPane.getChildren().add(btnLogin);
        idFlowPane.setHgap(10);

        flowpane.getChildren().add(btnDeposit);
        flowpane.getChildren().add(btnWithdraw);
        flowpane.getChildren().add(btnExit);


        //VBox.setMargin(idPane, new Insets(0, 20, 0, 20));
        vbox.getChildren().addAll(idFlowPane, depositField, withdrawField, flowpane, grid);

        return vbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        sceneTitle.setFont(Font.font("Helvetica", FontWeight.MEDIUM, 15));
        stage.setTitle("ZipCodeBank"); //sets the title of the scene

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
