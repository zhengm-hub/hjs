package cn.dceast.platform.paas.service.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 键的生成策略
 * 键的生成策略有两种，一种是默认策略，一种是自定义策略
 * 默认的key生成策略是通过KeyGenerator生成的，其默认策略如下：
 * 如果方法没有参数，则使用0作为key。
 * 如果只有一个参数的话则使用该参数作为key。
 * 如果参数大于一个的话则使用所有参数的hashCode作为key。
 * 如果我们需要指定自己的默认策略的话，那么我们可以实现自己的KeyGenerator，
 * 然后指定我们的Spring Cache使用的KeyGenerator为我们自己定义的KeyGenerator。
 * 即配置文件中：<cache:annotation-driven key-generator="keyGeneratorService" cache-manager="cacheManager"/>
 * ====================以上与以下区分==============
 * 自定义策略是指我们可以通过Spring的EL表达式来指定我们的key。
 * 这里的EL表达式可以使用方法参数及它们对应的属性。使用方法参数时我们可以直接使用“#参数名”或者“#p参数index”。
 * 这里不允许自由定义key如：随便写个 key="#jkjkl"
 * 下面是使用参数作为key的示例。--与service层的Cacheable属性key描述一致
 * @ Cacheable(value="users", key="#id")
 * public User find(Integer id) {
 * returnnull;
 * }
 */
//@Service
public class KeyGeneratorSer implements KeyGenerator {
	
	private static final Log logger = LogFactory.getLog(KeyGeneratorSer.class);

	/**
	 *
	 * @param target:当前被调用的对象
	 * @param method:当前方法
	 * @param params:当前方法参数
	 * @return
	 */
	/**
	 * =====================note=====================================
	 * ****在方法上缓存数据，如果不配置key参数，就使用自定义的默认key生成策略***
	 * ==============================================================
	 * @param target
	 * @param method
	 * @param params
	 * @return
	 */
	@Override
	public Object generate(Object target, Method method, Object... params) {
		/*String key = method.getName().toLowerCase() + "_" + StringUtils.join(params, "_");
		logger.debug("KEY：" + key);
		return key;*/
		//方法名作为缓存key
		String key = method.getName().toLowerCase();
		return key;
	}

	public static void main(String[] args) {

	}
}
