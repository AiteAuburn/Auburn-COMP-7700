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
     * @return 0 if success
     */
    public int withdraw(double amount){
        super.withdraw(amount);
        if( nBalance < 0 )
            mTotalFee += OVERDRAFT_FEE;
        return 0;
    }

}
