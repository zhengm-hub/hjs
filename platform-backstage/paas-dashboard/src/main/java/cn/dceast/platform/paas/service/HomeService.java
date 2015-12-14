/**
 * Project:platform-paas
 * <p/>
 * File:AppService.java
 * <p/>
 * Package:cn.dceast.platform.paas.service
 * <p/>
 * Date:15/7/31下午2:37
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/31下午2:37		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.service;

import cn.dceast.platform.paas.model.Home;
import cn.dceast.platform.paas.model.User;
import cn.dceast.platform.paas.repository.HomeMapper;
import cn.dceast.platform.paas.repository.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhming on 2015/10/30.
 */

/**
 * ======================缓存注释==================
 * @Cacheable 可以标记在一个方法上，也可以标记在一个类上。
 * 当标记在一个方法上时表示该方法是支持缓存的，
 * 当标记在一个类上时则表示该类所有的方法都是支持缓存的。
 * 对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，
 * 以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法。
 * Spring在缓存方法的返回值时是以键值对进行缓存的，值就是方法的返回结果，
 * 至于键的话，Spring又支持两种策略，默认策略和自定义策略，这个稍后会进行说明。
 * 需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的。
 * @Cacheable 可以指定三个属性:value,key和condition
 * value属性是必须指定的，其表示当前方法的返回值是会被缓存在哪个Cache上的，对应Cache的名称。
 * 其可以是一个Cache也可以是多个Cache，当需要指定多个Cache时其是一个数组。
 * key属性是用来指定Spring缓存方法的返回结果时对应的key的。
 * 该属性支持SpringEL表达式。当我们没有指定该属性时，Spring将使用默认策略生成key。
 * 我们这里先来看看自定义策略，至于默认策略会在后文单独介绍。
 * 自定义策略是指我们可以通过Spring的EL表达式来指定我们的key。
 * 这里的EL表达式可以使用方法参数及它们对应的属性。
 * 使用方法参数时我们可以直接使用“#参数名”或者“#p参数index”。
 * 如：@Cacheable(value="users", key="#id")public User find(Integer id) {}
 * 有的时候我们可能并不希望缓存一个方法所有的返回结果。
 * 通过condition属性可以实现这一功能。condition属性默认为空，表示将缓存所有的调用情形。
 * 其值是通过SpringEL表达式来指定的，当为true时表示进行缓存处理；
 * 当为false时表示不进行缓存处理，即每次调用该方法时该方法都会执行一次。condition="true/false"
 * 如：user的id为偶数时才会进行缓存:@Cacheable(value={"users"}, key="#user.id", condition="#user.id%2==0")
 * 在支持Spring Cache的环境下，对于使用@Cacheable标注的方法，Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，
 * 如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
 * @CachePut 也可以声明一个方法支持缓存功能。
 * 与@Cacheable不同的是使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，
 * 而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
 * @CachePut 也可以标注在类上和方法上,使用@CachePut时我们可以指定的属性跟@Cacheable是一样的。
 * @CacheEvict 是用来标注在需要清除缓存元素的方法或类上的。
 * 当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作。
 * @CacheEvict 可以指定的属性有value、key、condition、allEntries和beforeInvocation。
 * 其中value、key和condition的语义与@Cacheable对应的属性类似。
 * 即value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；key表示需要清除的是哪个key，如未指定则会使用默认策略生成的key；
 * condition表示清除操作发生的条件。下面我们来介绍一下新出现的两个属性allEntries和beforeInvocation
 * allEntries是boolean类型，表示是否需要清除缓存中的所有元素。默认为false，表示不需要。
 * 当指定了allEntries为true时，Spring Cache将忽略指定的key。
 * 有的时候我们需要Cache一下清除所有的元素，这比一个一个清除元素更有效率。
 * 清除操作默认是在对应方法成功执行之后触发的，
 * 即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。使用beforeInvocation可以改变触发清除操作的时间，
 * 当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素。
 *
 * =============
 * 用户操作，不涉及性能问题，这里都暂时不做缓存
 * ============
 */
@Service
public class HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Cacheable(value = "home")
    public List<Home> getHome(){
        return homeMapper.selectHome();
    }


//    /**
//     * add User
//     * @param user
//     */
//    public void addUser(User user) {
//        userMapper.insert(user);
//    }
//
//    /**
//     * select all Users
//     * 不定义key，就使用自定义的默认缓存key策略(getall)
//     * getAll()是不做分页的操作，但是一般都做分页，所以这里暂不设缓存
//     * @return
//     */
////    @Cacheable(value = "users")
//    public List<User> getAll() {
//        return userMapper.selectAll();
//    }
//
//    /**
//     * get User by id  default method from Mapper
//     * 使用key="#userId"自定义策略,暂不做缓存
//     * @param userId
//     * @return
//     */
////    @Cacheable(value = "users", key = "#userId")
//    public User getUserById(String userId) {
//        return userMapper.selectByPrimaryKey(userId);
//    }
//
//    /**
//     * get User by name
//     * @param userName
//     * @return
//     */
////    @Cacheable(value = "users",key = "#userName")
//    public User getUserByName(String userName) throws Exception{
//        return userMapper.selectUserByName(userName);
//    }
//
//    /**
//     * Paging--分页
//     * @param pageIndex
//     * @param pageSize
//     * @param orderBy
//     * @return
//     */
////    @Cacheable(value = "users")
//    public PageInfo<User> page(int pageIndex, int pageSize, String orderBy) {
//        PageHelper.startPage(pageIndex, pageSize);
//
//        Map paras = Maps.newHashMap();
//        paras.put("orderBy", orderBy);
//        List<User> users = userMapper.selectBy(paras);
//        PageInfo<User> pageInfo = new PageInfo(users);
//        return pageInfo;
//    }
//
//    /**
//     * 登录查询用户
//     * @param userName
//     * @param password
//     * @return
//     * @throws Exception
//     * @Cacheable
//     * 对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，
//     * 以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法。
//     * 可以指定三个属性:value,key和condition
//     * value属性是必须指定的，其表示当前方法的返回值是会被缓存在哪个Cache上的，对应Cache的名称。
//     * 其可以是一个Cache也可以是多个Cache，当需要指定多个Cache时其是一个数组。
//     * key属性是用来指定Spring缓存方法的返回结果时对应的key的。
//     * ==========
//     * 这里其实不需要缓存，因为简单查询不涉及性能问题，这里只是做个例子，方便以后需要用到缓存的地方参照使用
//     * ==========
//     */
////    @Cacheable(value = "users", key = "#userName")
//    public User getUserByNameAndPwd(String userName,String password) throws Exception{
//        User user = new User(userName,password);
//        return userMapper.selectOne(user);
//    }
//
//    /**
//     * @CacheEvict
//     * value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；
//     * key表示需要清除的是哪个key，如未指定则会使用默认策略生成的key
//     * ====================
//     * 清除操作默认是在对应方法成功执行之后触发的，
//     * 即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。使用beforeInvocation可以改变触发清除操作的时间，
//     * 当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素。
//     * @param user
//     * @return
//     */
////    @CacheEvict(value = "users", key = "#user.userId")
//    public int updateUser(User user){
//        return userMapper.updateByPrimaryKey(user);
//    }
//
//    /**
//     *
//     * @param userId
//     * @return
//     */
////    @CacheEvict(value = "users", key = "#userId")
//    public int deleteUser(String userId){
//        return userMapper.deleteByPrimaryKey(userId);
//    }

}
