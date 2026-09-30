package src.main.java.com.lld.splitwise.service;

import src.main.java.com.lld.splitwise.model.Expense;
import src.main.java.com.lld.splitwise.model.Group;
import src.main.java.com.lld.splitwise.model.Split;
import src.main.java.com.lld.splitwise.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SplitwiseService {

    public Group createGroup(String name, User createdBy){
        Group group = new Group(UUID.randomUUID().toString(),name);
        return group;
    }
    
    public void addMember(Group group, User user){
        group.addMember(user);
    }
    
    public Expense addExpense(Expense expense){
        validateExpense(expense.getGroupId(),expense.getPaidBy(),expense.getAmount(),expense.getSplits());
        return expense;
    }

    private void validateExpense(String groupId, User paidBy, BigDecimal amount, List<Split> splits) {
    }
}
