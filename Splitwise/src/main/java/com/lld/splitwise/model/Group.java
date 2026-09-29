package src.main.java.com.lld.splitwise.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final String groupId;
    private final String name;
    private final List<User> members;
    private  final List<Expense> expenses;
    private final BalanceSheet balanceSheet;


    public Group(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.balanceSheet = new BalanceSheet();
    }

    public void addMember(User user){
        if(hasMember(user)){
            throw  new IllegalArgumentException("User already exists is group");
        }
        members.add(user);
    }

    public boolean hasMember(User user){
        for (User member : members) {
            if(member.getId().equals(user.getId())){
                return true;
            }
        }
        return false;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }


}
