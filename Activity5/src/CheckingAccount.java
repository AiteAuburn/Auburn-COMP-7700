/**
 * Activity 5 - Checking Account
 * @author Ai-Te Kuo
 * @version Sep 12, 2019
 */

public class CheckingAccount extends Account{
    public static double OVERDRAFT_FEE = 35;
    public CheckingAccount(int no, double bal, String owner){
        super(no, bal, owner);
    }

    /**
     * Withdraw the amount of money and check if an overdraft occurs.
     * Check if the available balance goes below zero.
     * If so, charge an overdraft fee.
     * @param amount the amount of money to be withdrawn
     * @return true if success
     */
    public boolean withdraw(double amount){
        super.withdraw(amount);
        if( nBalance < 0 )
            nBalance -= OVERDRAFT_FEE;
        return true;
    }

    /**
     * Calculate the interest in this account.
     * Non-interest checking accounts , thus always return 0.
     * @return the interest
     */
    public double computeInterest(){
        return 0.0;
    }

    /**
     * Calculate the monthly maintenance fee.
     * Checking accounts have no monthly fee , thus always return 0.
     * @return the maintenance fee
     */
    public double computeFees(){
        return 0.0;
    }
}
