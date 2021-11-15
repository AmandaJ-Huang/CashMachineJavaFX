package rocks.zipcode.atm.bank;

/**
 * @author ZipCodeWilmington
 */
public final class AccountData {

    private final Integer id;
    private final String name;
    private final String email;

    private final float balance;

    AccountData(Integer id, String name, String email, float balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public Integer getId() {
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
            return "Account Id: " + id + '\n' +
                    "Name: " + name + '\n' +
                    "Email: " + email + '\n' +
                    "Balance: " + String.format("%,.02f", balance) + '\n' +
                    "Your account is overdrafted.";
        }
        return "Account Id: " + id + '\n' +
                "Name: " + name + '\n' +
                "Email: " + email + '\n' +
                "Balance: " + String.format("%,.02f", balance) + '\n';
    }
}
