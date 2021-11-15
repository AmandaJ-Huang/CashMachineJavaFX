package rocks.zipcode.atm.bank;

import javafx.scene.control.Alert;

/**
 * @author ZipCodeWilmington
 */
public abstract class Account {

    private AccountData accountData;

    public Account(AccountData accountData) {
        this.accountData = accountData;
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void deposit(Float amount) {
        canDepositOrWithDraw(amount);
        updateBalance(getBalance() + amount);
    }

    public boolean withdraw(Float amount) {
        if (canWithdraw(amount)) {
            canDepositOrWithDraw(amount);
            updateBalance(getBalance() - amount);
            return true;
        } else {
            return false;
        }
    }

    protected boolean canWithdraw(float amount) {
        return getBalance() >= amount;
    }

    public float getBalance() {
        return accountData.getBalance();
    }

    private void updateBalance(float newBalance) {
        accountData = new AccountData(accountData.getId(), accountData.getName(), accountData.getEmail(),
                newBalance);
    }

    protected void canDepositOrWithDraw(Float input) {
        if (input < 0.0) {
            Alert negativeInputAmount = new Alert(Alert.AlertType.WARNING);

            negativeInputAmount.setTitle("Invalid Deposit/Withdrawal");
            negativeInputAmount.setHeaderText("Invalid Amount Entered");
            negativeInputAmount.setContentText("Deposit/withdrawal cannot be less than $0. " +
                    "Please input a positive amount.");
            negativeInputAmount.showAndWait();
            return;
        }
    }
}
