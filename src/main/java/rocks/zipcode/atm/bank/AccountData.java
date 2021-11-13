package rocks.zipcode.atm.bank;

/**
 * @author ZipCodeWilmington
 */
public final class AccountData {

    private final int id;
    private final String name;
    private final String email;

    private final float balance;

    AccountData(int id, String name, String email, float balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        if (balance < 0) {
            return  "Account id: " + id + '\n' +
                    "Name: " + name + '\n' +
                    "Email: " + email + '\n' +
                    "Balance: " + balance + '\n' +
                    "Alert: Your account is overdrawn." + '\n' +
                    "You will not be able to withdraw any more until your account balance is $0 or more.";
        } else {
            return
                    "Account id: " + id + '\n' +
                    "Name: " + name + '\n' +
                    "Email: " + email + '\n' +
                    "Balance: " + balance;
        }
    }
}
