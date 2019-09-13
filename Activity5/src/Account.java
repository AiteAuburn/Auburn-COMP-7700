import java.util.HashMap;
import java.util.Map;

/**
 * Activity 5 - Checking Account
 * @author Ai-Te Kuo
 * @version Sep 12, 2019
 */

public class Account {
    protected int mAccountNumber;
    protected double nBalance;
    protected String nOwner;
    protected double mTotalFee;
    protected static final Map<Integer, String> ERROR_MSG = new HashMap<Integer, String>();
    static {
        ERROR_MSG.put(1, "Your bank account doesn't have enough money.");
        ERROR_MSG.put(2, "You have reached the maximum amount of withdrawals per month.");
        ERROR_MSG.put(3, "You cannot deposit a negative number.");
    }

    public Account(int no, double bal, String owner){
        mAccountNumber = no;
        nBalance = bal;
        nOwner = owner;
    }
    public int getAccountNumber(){ return mAccountNumber;}
    public double getBalance(){ return nBalance;}
    public String StringOwner(){ return nOwner;}

    /**
     * Given a error code, return a error message.
     * @param returnCode int , a return code
     * @return the error message corresponding to the error code
     */
    public static String getErrMsg(int returnCode) {
        if (ERROR_MSG.containsKey(returnCode))
            return ERROR_MSG.get(returnCode);
        else
            return "Unexpected";
    }

    /**
     * Deposit the amount of money.
     * @param amount the amount of money to be deposited
     * @return 0 if success
     */
    public int deposit(double amount){
        int returnCode = 0;
        if( amount <= 0)
            returnCode = 3;
        nBalance += amount;
        if ( returnCode != 0)
            System.out.println(getErrMsg(returnCode));
        return returnCode;
    }

    /**
     * Withdraw the amount of money.
     * @param amount the amount of money to be withdrawn
     * @return 0 if success
     */
    public int withdraw(double amount){
        nBalance -= amount;
        return 0;
    }

    /**
     * Calculate the interest in this account.
     * Default non-interest accounts, always return 0.
     * @return the interest
     */
    public double computeInterest(){
        return 0.0;
    }

    /**
     * Calculate the monthly maintenance fee plus overdraft fee.
     * Checking accounts have no monthly fee , thus always return the total of overdraft fee.
     * @return the total of overdraft fee
     */
    public double computeFees(){
        return mTotalFee;
    }


    /**
     * Give a brief description of the account
     * @return the account summary
     */
    public String toString() {
        return "Name: " + nOwner
                + "\n\tAccount Number: " + mAccountNumber
                + "\n\tBalance: " + nBalance
                + "\n\tInterest: " + computeInterest()
                + "\n\tFee: " + computeFees()
                + "\n";
    }
}
