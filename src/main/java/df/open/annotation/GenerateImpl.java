package df.open.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明:
 * <p/>
 * Copyright: Copyright (c)
 * <p/>
 * Company:
 * <p/>
 *
 * @author 付亮(OF2101)
 * @version 1.0.0
 * @date 2016/10/11
 */
@Target(ElementType.TYPE)//注解使用目标为类
@Retention(RetentionPolicy.SOURCE)//注解保留范围为源代码
public @interface GenerateImpl {
    String suffix() default "Impl";//生成对应接口的后缀名
}
