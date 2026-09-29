package src.main.java.com.lld.splitwise.strategy;

import src.main.java.com.lld.splitwise.model.Split;
import src.main.java.com.lld.splitwise.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements SplitStrategy{

    @Override
    public List<Split> calculate(BigDecimal amount, List<User> participants) {
        if(participants==null || participants.isEmpty()){
            throw new IllegalArgumentException("Participants cannot be empty");
        }

        //Divide the total expense equally among all participants, rounded to 2 decimal
        BigDecimal splitAmount = amount.divide(BigDecimal.valueOf(participants.size()), 2, BigDecimal.ROUND_HALF_UP);
        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            splits.add(new Split(user,splitAmount));
        }
        return splits;
    }
}
