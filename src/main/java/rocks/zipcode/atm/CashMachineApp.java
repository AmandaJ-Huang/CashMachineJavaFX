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
import javafx.scene.control.Menu;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField idField = new TextField();
    private TextField depositWithdrawField = new TextField();
    private Text welcomeTitle = new Text("Welcome to ZipCodeBank's ATM.");
    private TextField newLoginID = new TextField();
    private TextField newName = new TextField();
    private TextField newBalance = new TextField();
    private TextField newEmail = new TextField();

    private CashMachine cashMachine = new CashMachine(new Bank());

    private Button btnLogin = new Button("Login");
    private Button btnDeposit = new Button("Deposit");
    private Button btnWithdraw = new Button("Withdraw");
    private Button btnExit = new Button("Logout");
    private Button btnRegister = new Button("Register");
    private Button btnYes = new Button("Yes");
    private Button btnNo = new Button("No");

    private Menu accountMenu = new Menu("Account Listing");
    private MenuBar menuBar = new MenuBar();

    private GridPane grid = new GridPane();

    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 500);

        //Account listing via menu
        accountListingMenu();

        TextArea areaInfo = new TextArea();

        //Button actions for Login, Deposit, Withdraw, Logout
        enableDisableButtons("on");
        btnLogin.setOnAction(e -> {
            Boolean accountExists = loginWarning(idField);
            if (accountExists){
                int id = Integer.parseInt(idField.getText());
                cashMachine.login(id);
                newLoginID.setText(cashMachine.toString());
                enableDisableButtons("off");
            }
        });

        btnRegister.setOnAction(e -> {
            registerNewAccount();
                });

        btnDeposit.setOnAction(e -> {
            Float amount = Float.parseFloat(depositWithdrawField.getText());
            cashMachine.deposit(amount);

            //areaInfo.setText(cashMachine.toString());
        });

        btnWithdraw.setOnAction(e -> {
            Float amount = Float.parseFloat(depositWithdrawField.getText());
            cashMachine.withdraw(amount);

            //areaInfo.setText(cashMachine.toString());
        });

        btnExit.setOnAction(e -> {
            cashMachine.exit();

            areaInfo.setText(cashMachine.toString());
            enableDisableButtons("on");
        });


        //FlowPanes - id, deposit, withdraw
        FlowPane loginPane = new FlowPane(10, 0, idField, btnLogin, btnRegister);
        loginPane.setAlignment(Pos.CENTER);

        FlowPane depositAndWithdrawalPane = new FlowPane(10, 0, depositWithdrawField, btnDeposit, btnWithdraw);
        depositAndWithdrawalPane.setAlignment(Pos.CENTER_LEFT);


        //Formatting
        idField.setPromptText("Enter ID number.");
        depositWithdrawField.setPromptText("Enter amount to deposit or withdraw.");
        welcomeTitle.setFont(Font.font("Helvetica", FontWeight.MEDIUM, 15));

        //vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(welcomeTitle, new Insets(5, 20, 0, 20));
        VBox.setMargin(depositAndWithdrawalPane, new Insets(0, 20, 0, 20));
        vbox.getChildren().addAll(menuBar, welcomeTitle, loginPane, depositAndWithdrawalPane, btnExit);

        return vbox;
    }

    private Boolean loginWarning(TextField input) {
        Alert loginWarning = new Alert(Alert.AlertType.WARNING);

        if (input.getText().isEmpty()) {
            loginWarning.setTitle("Login Warning: No ID");
            loginWarning.setHeaderText("You have not entered an account ID."
                    + '\n' + "Please try again.");
            loginWarning.showAndWait();
            return false;
        }
        for(Integer account: cashMachine.listAccounts()){
            if (account.equals(Integer.parseInt(input.getText()))) {
                return true;
            }
        }
        loginWarning.setTitle("Login Warning: Invalid ID");
        loginWarning.setHeaderText("You have entered an invalid account ID."
                + '\n' + "Please try again.");
        loginWarning.showAndWait();
        return false;
    }

    private void accountListingMenu() {
        menuBar.getMenus().add(accountMenu);
        for(Integer account : cashMachine.listAccounts()) {
            MenuItem addAccount = new MenuItem(account.toString());
            accountMenu.getItems().add(addAccount);
        }
    }

    private void enableDisableButtons(String onOff) {
        if (onOff.toLowerCase() == "on") {
            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);
            btnExit.setDisable(true);
            btnLogin.setDisable(false);

            idField.setDisable(false);
            depositWithdrawField.setDisable(true);
        } else {
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
            btnExit.setDisable(false);
            btnLogin.setDisable(true);

            idField.setDisable(true);
            depositWithdrawField.setDisable(false);
        }
    }

    private void registerNewAccount() {
        Dialog dialog = new Dialog();
        TextInputDialog registrationForm = new TextInputDialog();

        dialog.setTitle("Register New Account");
        dialog.setHeaderText("Please fill out the form to register " +
                "a new account with ZipCodeBank.");

        formLayout(dialog);
        dialog.show();
    }

    private void formLayout(Dialog dialog) {
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        //labels to indicate corresponding data fields
        Label loginIDLabel = new Label("Enter login ID: ");
        Label nameLabel = new Label("Enter name: ");
        Label emailLabel = new Label("Enter email: ");
        Label balanceLabel = new Label("Initial deposit: ");

        //grid layout
        grid.add(loginIDLabel, 0, 1);
        grid.add(nameLabel, 0, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(balanceLabel, 0, 4);

        grid.add(newLoginID, 1, 1);
        grid.add(newName, 1, 2);
        grid.add(newEmail, 1, 3);
        grid.add(newBalance, 1, 4);

        dialog.getDialogPane().setContent(grid);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setTitle("ZipCodeBank"); //sets the title of the scene
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
