package jun.spring.sqlservice;

import jun.spring.sqlservice.registry.UpdatableSqlRegistry;
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
