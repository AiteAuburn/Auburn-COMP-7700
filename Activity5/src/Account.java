/**
 * Activity 5 - Checking Account
 * @author Ai-Te Kuo
 * @version Sep 12, 2019
 */

public class Account {
    protected int mAccountNumber;
    protected double nBalance;
    protected String nOwner;
    public Account(int no, double bal, String owner){
        mAccountNumber = no;
        nBalance = bal;
        nOwner = owner;
    }
    public int getAccountNumber(){ return mAccountNumber;}
    public double getBalance(){ return nBalance;}
    public String StringOwner(){ return nOwner;}

    /**
     * Deposit the amount of money.
     * @param amount the amount of money to be deposited
     * @return true if success
     */
    public boolean deposit(double amount){
        if( amount <= 0)
            return false;
        nBalance += amount;
        return true;
    }

    /**
     * Withdraw the amount of money.
     * @param amount the amount of money to be withdrawn
     * @return true if success
     */
    public boolean withdraw(double amount){
        nBalance -= amount;
        return true;
    }

    /**
     * Give a brief description of the account
     * @return the account summary
     */
    public String toString(){
        return "Name:" + nOwner + "\tAccount Number:" + mAccountNumber + "\tBalance:" + nBalance;
    }
}
