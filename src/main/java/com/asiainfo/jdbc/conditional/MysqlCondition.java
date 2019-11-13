package com.asiainfo.jdbc.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 
 * @Description: @Conditional 多数据库支持
 * 
 * @author chenzq  
 * @date 2019年7月13日 下午2:20:02
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class MysqlCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata typeMeta) {
		String dbType = context.getEnvironment().getProperty("spring.environment.dbtype");
		if("MYSQL".equals(dbType.toUpperCase())){
			return true;
		}
		return false;
	}
}
