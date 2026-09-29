package src.main.java.com.lld.splitwise.strategy;

import src.main.java.com.lld.splitwise.model.Split;
import src.main.java.com.lld.splitwise.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface SplitStrategy {

    List<Split> calculate(BigDecimal amount, List<User> participants);
}
