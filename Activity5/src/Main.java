public class Main {
    public static void main(String args[]){
        Account acc = new SavingsAccount(101,100.0,"Aite");
        System.out.println(acc);
        acc.deposit(900);
    }
}
