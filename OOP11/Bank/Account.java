package Bank;

import java.util.UUID;

public class Account {
    private UUID IBAN;
    private String name;
    private double balance;
    private Boolean isBusiness;

    public Account(String name, Boolean isBusiness){
        this.name=name;
        balance = 0;
        IBAN = UUID.randomUUID();
        this.isBusiness = isBusiness;
    }

    public void add(double amount){
        balance = balance+amount;
    }

    public void remove(double amount){
        if(balance - amount < 0){
            throw new ArithmeticException("Insufficient account balance.");
        }
        else{
            balance = balance-amount;
        }
    }
}
