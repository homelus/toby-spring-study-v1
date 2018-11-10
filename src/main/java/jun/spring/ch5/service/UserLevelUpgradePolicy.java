package jun.spring.ch5.service;

import jun.spring.ch5.model.User;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);

}
