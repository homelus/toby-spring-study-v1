package jun.spring.ch6.service;

import jun.spring.ch6.model.User;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);

}
