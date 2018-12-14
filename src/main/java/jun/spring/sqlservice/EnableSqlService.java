package jun.spring.sqlservice;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SqlServiceContext.class)
public @interface EnableSqlService {
}
