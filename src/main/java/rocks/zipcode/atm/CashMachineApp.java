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

    private CashMachine cashMachine = new CashMachine(new Bank());

    private Button btnLogin = new Button("Login");
    private Button btnDeposit = new Button("Deposit");
    private Button btnWithdraw = new Button("Withdraw");
    private Button btnExit = new Button("Logout");
    private Button btnRegister = new Button("Register");

    private Menu accountMenu = new Menu("Account Listing");
    private MenuBar menuBar = new MenuBar();

    //for formLayout GridPane
    private GridPane formGrid = new GridPane();
    private TextField newLoginID = new TextField();
    private TextField newName = new TextField();
    private TextField newBalance = new TextField();
    private TextField newEmail = new TextField();
    private RadioButton basicAccountRadio = new RadioButton("Basic");
    private RadioButton premiumAccountRadio = new RadioButton("Premium");
    private ToggleGroup accountSelectionGroup = new ToggleGroup();

    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 500);
        TextArea areaInfo = new TextArea();

        //Register form formatting
        registerFormLayout();
        basicAccountRadio.setToggleGroup(accountSelectionGroup);
        premiumAccountRadio.setToggleGroup(accountSelectionGroup);

        //Account listing via menu
        menuBar.getMenus().add(accountMenu);
        accountListingMenu();

        //Button actions for Login, Deposit, Withdraw, Logout
        enableDisableButtons("on");
        btnLogin.setOnAction(e -> {
            Boolean accountExists = loginWarning(idField);
            if (accountExists){
                int id = Integer.parseInt(idField.getText());
                cashMachine.login(id);
                areaInfo.setText(cashMachine.toString());
                enableDisableButtons("off");
            }
        });

        btnRegister.setOnAction(e -> {
            registerNewAccount();
            accountListingMenu();
                });

        btnDeposit.setOnAction(e -> {
            Float amount = Float.parseFloat(depositWithdrawField.getText());
            cashMachine.deposit(amount);

            areaInfo.setText(cashMachine.toString());
        });

        btnWithdraw.setOnAction(e -> {
            Float amount = Float.parseFloat(depositWithdrawField.getText());
            cashMachine.withdraw(amount);

            areaInfo.setText(cashMachine.toString());
        });

        btnExit.setOnAction(e -> {
            cashMachine.exit();

            areaInfo.clear();
            enableDisableButtons("on");
        });


        //FlowPanes - id, deposit, withdraw, account detail
        FlowPane loginPane = new FlowPane(10, 0, idField, btnLogin, btnRegister);
        loginPane.setAlignment(Pos.CENTER);

        FlowPane depositAndWithdrawalPane = new FlowPane(10, 0, depositWithdrawField, btnDeposit, btnWithdraw);
        depositAndWithdrawalPane.setAlignment(Pos.CENTER_LEFT);

        FlowPane accountDetailVertical = new FlowPane();

        //Formatting
        idField.setPromptText("Enter ID number.");
        depositWithdrawField.setPromptText("Enter amount to deposit or withdraw.");
        welcomeTitle.setFont(Font.font("Helvetica", FontWeight.MEDIUM, 15));

        //vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(welcomeTitle, new Insets(5, 20, 0, 20));
        VBox.setMargin(depositAndWithdrawalPane, new Insets(0, 20, 0, 20));
        vbox.getChildren().addAll(menuBar, welcomeTitle, loginPane, depositAndWithdrawalPane, areaInfo, btnExit);

        return vbox;
    }

    private Boolean loginWarning(TextField input) {
        Alert loginWarning = new Alert(Alert.AlertType.WARNING);

        if (input.getText().isEmpty()) {
            loginWarning.setTitle("Login Warning: No ID");
            loginWarning.setHeaderText("No ID Provided");
            loginWarning.setContentText("You have not entered an account ID."
                    + '\n' + "Please check the account listing and try again.");
            loginWarning.showAndWait();
            return false;
        }
        try{
            for(Integer account: cashMachine.listAccounts()){
                if (account.equals(Integer.parseInt(input.getText()))) {
                    return true;
                }
            }
        } catch (Exception e) {
            loginWarning.setTitle("Login Warning: Invalid ID");
            loginWarning.setHeaderText("Invalid ID Provided");
            loginWarning.setContentText("You have entered an invalid account ID."
                    + '\n' + "Please try again or register a new account.");
            loginWarning.showAndWait();
            return false;
        };
        loginWarning.setTitle("Login Warning: Invalid ID");
        loginWarning.setHeaderText("Invalid ID Provided");
        loginWarning.setContentText("You have entered an invalid account ID."
                + '\n' + "Please try again or register a new account.");
        loginWarning.showAndWait();
        return false;
    }

    private void accountListingMenu() {
        if(!accountMenu.getItems().isEmpty()){
            accountMenu.getItems().clear();
        }
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
            btnRegister.setDisable((false));

            idField.setDisable(false);
            depositWithdrawField.setDisable(true);
        } else {
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
            btnExit.setDisable(false);
            btnLogin.setDisable(true);
            btnRegister.setDisable(true);

            idField.setDisable(true);
            depositWithdrawField.setDisable(false);
        }
    }

    private void registerNewAccount() {
        Dialog dialog = new Dialog();

        dialog.setTitle("Register New Account");
        dialog.setHeaderText("Please fill out the form to register " +
                "a new account with ZipCodeBank.");

        dialog.getDialogPane().setContent(formGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                int registeredLoginID = Integer.parseInt(newLoginID.getText());
                String registeredName = newName.getText();
                String registeredEmail = newEmail.getText();
                Float registeredBalance = Float.parseFloat(newBalance.getText());

                if (accountSelectionGroup.getSelectedToggle().equals(basicAccountRadio)) {
                    cashMachine.addBasicAccount(registeredLoginID, registeredName, registeredEmail, registeredBalance);
                    newLoginID.clear();
                    newName.clear();
                    newEmail.clear();
                    newBalance.clear();
                }

                if (accountSelectionGroup.getSelectedToggle().equals(premiumAccountRadio)) {
                    cashMachine.addPremiumAccount(registeredLoginID, registeredName, registeredEmail, registeredBalance);
                    newLoginID.clear();
                    newName.clear();
                    newEmail.clear();
                    newBalance.clear();

                }
            }
        });

    }

    private void registerFormLayout() {
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20, 20, 20, 20));

        //labels to indicate corresponding data fields
        Label loginIDLabel = new Label("Enter Login ID: ");
        Label nameLabel = new Label("Enter Name: ");
        Label emailLabel = new Label("Enter Email: ");
        Label balanceLabel = new Label("Initial Deposit: ");
        Label selectAccountType = new Label("Select Account Type: ");

        //formGrid layout
        formGrid.add(selectAccountType, 1, 1);
        formGrid.add(loginIDLabel, 1, 3);
        formGrid.add(nameLabel, 1, 4);
        formGrid.add(emailLabel, 1, 5);
        formGrid.add(balanceLabel, 1, 6);

        newLoginID.setPromptText("example: 007");
        newName.setPromptText("example: James Bond");
        newEmail.setPromptText("example@email.com");
        newBalance.setPromptText("example: 500");

        formGrid.add(basicAccountRadio, 2, 1);
        formGrid.add(premiumAccountRadio, 2, 2);
        formGrid.add(newLoginID, 2, 3);
        formGrid.add(newName, 2, 4);
        formGrid.add(newEmail, 2, 5);
        formGrid.add(newBalance, 2, 6);
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
