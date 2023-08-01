package Service;

import Model.Account;
import DAO.AccountDao;

public class AccountService {

    private AccountDao accountDao;

    public AccountService() {
        accountDao = new AccountDao();
    }

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account addAccount(Account account) {
        if (account.getPassword().length() >= 4 && !account.getUsername().equals("")
                && accountDao.findUsername(account.getUsername()) == null) {
            return accountDao.insertAccount(account);
        }
        return null;

    }

    public Account loginAccount(Account account) {
        return accountDao.selectAccount(account);
    }

}
