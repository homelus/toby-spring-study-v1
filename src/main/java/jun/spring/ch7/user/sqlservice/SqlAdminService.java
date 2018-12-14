package jun.spring.ch7.user.sqlservice;

import jun.spring.ch7.user.sqlservice.registry.UpdatableSqlRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqlAdminService {

    @Autowired
    private UpdatableSqlRegistry updatableSqlRegistry;

    public void setUpdatableSqlRegistry(UpdatableSqlRegistry updatableSqlRegistry) {
        this.updatableSqlRegistry = updatableSqlRegistry;
    }

    public void updateEventListener(String key, String value) {
        this.updatableSqlRegistry.updateSql(key, value);
    }
}
