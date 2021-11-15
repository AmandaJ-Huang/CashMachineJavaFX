package rocks.zipcode.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;

import java.awt.*;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField loginIdField = new TextField();
    private TextField depositWithdrawField = new TextField();

    private CashMachine cashMachine = new CashMachine(new Bank());

    private Button btnLogin = new Button("Login");
    private Button btnDeposit = new Button("Deposit");
    private Button btnWithdraw = new Button("Withdraw");
    private Button btnExit = new Button("Logout");
    private Button btnRegister = new Button("Register");

    private Menu accountMenu = new Menu("Account Listing");
    private MenuBar menuBar = new MenuBar();

    //Vbox Elements
    private GridPane welcomeTitleGrid = new GridPane();
    private Text loginInstructions = new Text("Please login (see Account Listing) or register for a new account.");
    private Text welcomeTitle = new Text("Welcome to ZipCodeBank's ATM.");
    private FlowPane
            loginInstrPane,
            loginPane,
            loginAccountInfoDivider,
            depositWithdrawFieldPane,
            depositWithdrawBtnPane;

    //for formLayout GridPane
    private GridPane formGrid = new GridPane();
    private TextField newLoginID = new TextField();
    private TextField newName = new TextField();
    private TextField newBalance = new TextField();
    private TextField newEmail = new TextField();
    private RadioButton basicAccountRadio = new RadioButton("Basic");
    private RadioButton premiumAccountRadio = new RadioButton("Premium");
    private ToggleGroup accountSelectionGroup = new ToggleGroup();

    //for Account Information GridPane
    private GridPane accountInfoGrid = new GridPane();
    private Text accountIDText = new Text("Account ID: ");
    private Text accountTypeText = new Text("Account Type: ");
    private Text nameText = new Text("Name: ");
    private Text emailText = new Text("Email: ");
    private Text balanceText = new Text("Balance: ");

    private TextArea areaInfo = new TextArea();

    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 500);

        //Setting up account info grid
        accountInfoGridLayout();

        //Register form formatting
        registerFormLayout();
        basicAccountRadio.setToggleGroup(accountSelectionGroup);
        premiumAccountRadio.setToggleGroup(accountSelectionGroup);

        //Account listing via menu
        menuBar.getMenus().add(accountMenu);
        accountListingMenu();

        //Button actions for Login, Deposit, Withdraw, Logout
        enableDisableButtons("on");

        btnLogin.setOnAction(e -> loginAttempt(loginIdField));

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

            loginIdField.clear();
            depositWithdrawField.clear();
            areaInfo.clear();
            enableDisableButtons("on");
        });


        //GP - Welcome Text
        vboxLayoutAndFormatting();
        Separator welcomeSeparator = new Separator();

        //VB - adding all elements
        vbox.getChildren().addAll(menuBar, welcomeTitleGrid, welcomeSeparator, loginInstrPane, loginPane,
                loginAccountInfoDivider, depositWithdrawFieldPane, depositWithdrawBtnPane);

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
            for(Integer account: cashMachine.getBank().bankAccountList()){
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
        for(Integer account : cashMachine.getBank().bankAccountList()) {
            MenuItem addAccount = new MenuItem(account.toString());
            accountMenu.getItems().add(addAccount);

            addAccount.setOnAction(event -> {
                loginIdField.setText(account.toString());
                loginAttempt(loginIdField);
            });
        }
    }

    private void enableDisableButtons(String onOff) {
        if (onOff.toLowerCase() == "on") {
            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);
            btnExit.setDisable(true);
            btnLogin.setDisable(false);
            btnRegister.setDisable((false));

            loginIdField.setDisable(false);
            depositWithdrawField.setDisable(true);
        } else {
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
            btnExit.setDisable(false);
            btnLogin.setDisable(true);
            btnRegister.setDisable(true);

            loginIdField.setDisable(true);
            depositWithdrawField.setDisable(false);
        }
    }

    private void loginAttempt(TextField input) {
        Boolean accountExists = loginWarning(input);
        if (accountExists) {
            int id = Integer.parseInt(input.getText());
            cashMachine.login(id);
            areaInfo.setText(cashMachine.toString());
            enableDisableButtons("off");
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
                    cashMachine.getBank().addNewBasicAccount(registeredLoginID,
                            registeredName, registeredEmail, registeredBalance);

                    newLoginID.clear();
                    newName.clear();
                    newEmail.clear();
                    newBalance.clear();
                }

                if (accountSelectionGroup.getSelectedToggle().equals(premiumAccountRadio)) {
                    cashMachine.getBank().addNewPremiumAccount(registeredLoginID,
                            registeredName, registeredEmail, registeredBalance);

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

    private void accountInfoGridLayout() {
        accountInfoGrid.setAlignment(Pos.BOTTOM_LEFT);
        accountInfoGrid.setHgap(10);
        accountInfoGrid.setVgap(10);
        accountInfoGrid.setPadding(new Insets(10, 10, 10, 10));

        //accountInfoGrid layout
        accountInfoGrid.add(accountIDText, 0, 0);
        accountInfoGrid.add(accountTypeText, 0, 1);
        accountInfoGrid.add(nameText, 0, 2);
        accountInfoGrid.add(emailText, 0, 3);
        accountInfoGrid.add(balanceText, 0, 4);

        //accountInfoGrid.add();
    }

    private void vboxLayoutAndFormatting() {
        /*
        FP = FlowPane
        GP = GridPane
        VB = VBox element
        OT = Other element

        VB Formatting:
            > Font -- Tahoma
            > Background Color --

        VB Layout:
            > OT -- Menu Bar
            > GP -- Welcome Text (cosmetic, centered, bold)
            > FP -- Login Instructions
            > FP -- Login TextField, Login Button, Register Button (functional, centered)
            > VB -- Divider (cosmetic, across VB width)
            > VB -- Instructional Text (cosmetic, aligned left)
            > FP -- Deposit/Withdraw TextField (functional, across VB width)
            > FP -- Deposit Button, Withdraw Button (functional, aligned left)
            > GP -- Account Detail GP (cosmetic, aligned center)
            > FP -- Logout Button (functional, aligned right)

         */

        welcomeTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

        welcomeTitleGrid.setAlignment(Pos.CENTER);
        welcomeTitleGrid.add(welcomeTitle, 0, 0);
        VBox.setMargin(welcomeTitleGrid, new Insets(10, 0, 0, 0));

        //FP - Login/Register Instructions
        loginInstructions.setFont(Font.font("Tahoma", FontWeight.THIN, 12));
        loginInstrPane = new FlowPane(loginInstructions);
        VBox.setMargin(loginInstrPane, new Insets(10, 0, 0, 15));

        //FP - Login TextField, Login and Register buttons
        loginIdField.setPromptText("Enter ID number.");
        loginPane = new FlowPane(10, 0, loginIdField, btnLogin, btnRegister);
        loginPane.setAlignment(Pos.CENTER);
        VBox.setMargin(loginPane, new Insets(5, 0, 0, 15));

        //FP - Divider
        Text dividerText = new Text("DIVIDER");
        dividerText.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        loginAccountInfoDivider = new FlowPane(dividerText);
        loginAccountInfoDivider.setStyle("-fx-background-color: #000000");

        //FP - Deposit and Withdraw TextField
        depositWithdrawField.setPromptText("Enter amount to deposit or withdraw.");
        depositWithdrawField.setPrefWidth(500.0);
        depositWithdrawFieldPane = new FlowPane(depositWithdrawField);

        //FP - Deposit and Withdraw Buttons
        depositWithdrawBtnPane = new FlowPane(10, 0, btnDeposit, btnWithdraw);
        depositWithdrawBtnPane.setAlignment(Pos.CENTER_LEFT);

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setTitle("ZipCodeBank");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
