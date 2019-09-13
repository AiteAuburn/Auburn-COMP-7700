/**
 * Activity 5 - Checking Account
 * @author Ai-Te Kuo
 * @version Sep 12, 2019
 */

public class SavingsAccount extends Account{
    protected static double FIXED_MONTHLY_FEE = 100.0;
    protected static int MAX_MONTHLY_WITHDRAW_TIMES = 10;
    protected static double LEAST_MONTHLY_DEPOSIT = 100.0;
    protected static double INTEREST_RATE = 0.02;
    protected double monthlyDeposit = 0;
    protected int monthlyWithdrawalTimes = 0;
    public SavingsAccount(int no, double bal, String owner){
        super(no,bal,owner);
    }

    /**
     * Deposit the amount of money, also records the monthly deposit.
     * @param amount the amount of money to be deposited
     * @return 0 if success
     */
    public int deposit(double amount){
        super.deposit(amount);
        monthlyDeposit += amount;
        return 0;
    }

    /**
     * Withdraw the amount of money.
     * Check if the available balance will go below 0, and check if it reaches over the maximum withdrawal times.
     * If so, reject the withdrawal.
     * If success, increment the monthlyWithdrawalTimes by 1.
     * @param amount the amount of money to be withdrawn
     * @return 0 if success else error code
     */
    public int withdraw(double amount){
        int returnCode = 0;
        if( nBalance < amount )
            returnCode = 1;
        else if ( monthlyWithdrawalTimes >= MAX_MONTHLY_WITHDRAW_TIMES)
            returnCode = 2;
        else {
            super.withdraw(amount);
            monthlyWithdrawalTimes += 1;
            returnCode = 0;
        }

        if ( returnCode != 0)
            System.out.println(getErrMsg(returnCode));
        return returnCode;
    }

    /**
     * Calculate the interest in this account.
     * Easy formula: interest = deposit * interest rate
     * @return the interest
     */
    public double computeInterest(){
        return nBalance * INTEREST_RATE;
    }

    /**
     * Calculate the monthly maintenance fee.
     * Savings accounts have a fixed monthly fee (FIXED_MONTHLY_FEE).
     * The fee is waived if the customer deposited over M(LEASE_MONTHLY_DEPOSIT) this month.
     * @return the maintenance fee
     */
    public double computeFees(){
        if (monthlyDeposit >= LEAST_MONTHLY_DEPOSIT)
            return mTotalFee = 0.0;
        else
            return mTotalFee = FIXED_MONTHLY_FEE;
    }

    /**
     * Give a brief description of a savings account
     * @return the account summary
     */
    public String toString(){
        return "Name: " + nOwner
                + "\n\tAccount Number: " + mAccountNumber
                + "\n\tBalance: " + nBalance
                + "\n\tWithdrawal Times: " + monthlyWithdrawalTimes + "/" + MAX_MONTHLY_WITHDRAW_TIMES
                + "\n\tMonthly Deposit: " + monthlyDeposit + "/" + LEAST_MONTHLY_DEPOSIT
                + "\n\tInterest: " + computeInterest()
                + "\n\tFee: " + computeFees()
                + "\n";
    }
}
