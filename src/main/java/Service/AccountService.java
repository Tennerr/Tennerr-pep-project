package Service;

import java.util.List;

import DAO.AccountDAO;

import Model.Account;
import Model.Message;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account){
        return accountDAO.createAccount(account);

    }

    public Account verifyLogin(String username, String password)
    {
        return accountDAO.verifyLogin(username, password);
    }

    public List<Message> getMessageByAccountId(int account_id){
        return accountDAO.getMessageByAccountId(account_id);
    }

}
