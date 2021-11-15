package rocks.zipcode.atm.bank;

import rocks.zipcode.atm.ActionResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author ZipCodeWilmington
 */
public class Bank {

    private Map<Integer, Account> accounts = new TreeMap<>();

    public Bank() {
        accounts.put(1000, new BasicAccount(new AccountData(
                1000, "Example 1", "example1@gmail.com", 500
        )));

        accounts.put(2000, new PremiumAccount(new AccountData(
                2000, "Example 2", "example2@gmail.com", 200
        )));

        addNewBasicAccount(3000, "Harry Potter", "boywholived@gmail.com", 319995F);
        addNewBasicAccount(5555, "Spongebob SquarePants", "imrdy@bikinibottom.com", 1F);
        addNewPremiumAccount(1234, "Dr. Evil", "drevil@villains.com", 1000000F);
        addNewPremiumAccount(1111, "John Wick", "daisy@gmail.com", 1111111111F);
    }

    public void addNewBasicAccount(int id, String name, String email, Float balance) {
        accounts.put(id, new BasicAccount(new AccountData(id, name, email, balance)));
    }

    public void addNewPremiumAccount(int id, String name, String email, Float balance) {
        accounts.put(id, new PremiumAccount(new AccountData(id, name, email, balance)));
    }

    public Set<Integer> bankAccountList() {
        return accounts.keySet();
    }

    public ActionResult<AccountData> getAccountById(int id) {
        Account account = accounts.get(id);

        if (account != null) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("No account with id: " + id + "\nCheck Account Listing and try again.");
        }
    }

    public ActionResult<AccountData> deposit(AccountData accountData, Float amount) {
        Account account = accounts.get(accountData.getId());
        account.deposit(amount);

        return ActionResult.success(account.getAccountData());
    }

    public ActionResult<AccountData> withdraw(AccountData accountData, Float amount) {
        Account account = accounts.get(accountData.getId());
        boolean ok = account.withdraw(amount);

        if (ok) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("Withdraw failed: " + amount + ". Account has: " + account.getBalance());
        }
    }
}
