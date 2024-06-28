package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

   public Account register(Account account) {
    // Check if username is not blank, password is at least 4 characters long, and username is not already taken
    if (account.getUsername() != null && !account.getUsername().isEmpty() &&
        account.getPassword() != null && account.getPassword().length() >= 4 &&
        accountDAO.getAccountByUsername(account.getUsername()) == null) {
        // Create the account
        return accountDAO.createAccount(account);
    }
    return null; // Return null if registration fails any condition
}
    
}
