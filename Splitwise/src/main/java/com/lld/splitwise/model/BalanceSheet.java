package src.main.java.com.lld.splitwise.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {

    // Key: "debtorId->creditorId"
    // Value: amount debtor owes creditor
    //
    // Example:
    // "U2->U1" = 1000
    // means U2 owes U1 ₹1000
    private final Map<String, BigDecimal> balances = new HashMap<>();
    //We can use like this,but this can be complex using computeIfAbsent and merge methods
    //private final Map<String, Map<String, BigDecimal>> balances = new HashMap<>();

    public void addExpense(Expense expense){
        User payer = expense.getPaidBy();

        for (Split split : expense.getSplits()) {
            User participant = split.getUser();
            //A user cannot owe money to themselves
            if(participant.getId().equals(payer.getId())){
                continue;
            }
            addDebt(participant.getId(),payer.getId(),split.getAmount());
        }
    }

    private void addDebt(String debtorId, String creditorId, BigDecimal amount) {
        //if we use
        //private final Map<String, Map<String, BigDecimal>> balances = new HashMap<>();
        //We can use computeIfAbsent,and merge, but that can be complex to understand,do try

        String key = createKey(debtorId,creditorId);
        BigDecimal currentBalance = balances.getOrDefault(key,BigDecimal.ZERO);
        balances.put(key,currentBalance.add(amount));
    }

    public BigDecimal getBalance(String debtorId, String creditorId){
        String key = createKey(debtorId,creditorId);
        return balances.getOrDefault(key,BigDecimal.ZERO);
    }


    private String createKey(String debtorId, String creditorId) {
        return debtorId + "->" + creditorId;
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }

}
