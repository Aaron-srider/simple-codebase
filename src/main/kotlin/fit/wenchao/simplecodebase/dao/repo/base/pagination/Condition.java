package fit.wenchao.simplecodebase.dao.repo.base.pagination;

import fit.wenchao.simplecodebase.dao.repo.base.BaseDao;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Condition {
    BaseDao.ConditionEnum con() default BaseDao.ConditionEnum.EQ;

    String dbFieldName() default "";
}