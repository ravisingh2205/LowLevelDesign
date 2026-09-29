package src.main.java.com.lld.splitwise.model;

import src.main.java.com.lld.splitwise.enums.SplitType;

import java.math.BigDecimal;
import java.util.List;

public class Expense {

    private final String expenseId;
    private final String description;
    private final String groupId;
    private final User paidBy;
    private final BigDecimal amount;
    private final List<Split> splits;
    private final SplitType splitType;

    public Expense(String expenseId, String description, String groupId, User paidBy, BigDecimal amount, List<Split> splits, SplitType splitType) {
        this.expenseId = expenseId;
        this.description = description;
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.splits = splits;
        this.splitType = splitType;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupId() {
        return groupId;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public SplitType getSplitType() {
        return splitType;
    }
}
