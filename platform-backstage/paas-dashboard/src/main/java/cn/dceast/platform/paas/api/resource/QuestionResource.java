
package cn.dceast.platform.paas.api.resource;

import cn.dceast.platform.paas.api.MediaTypes;
import cn.dceast.platform.paas.model.User;
import cn.dceast.platform.paas.service.UserService;
import cn.dceast.platform.paas.utils.Constants;
import cn.dceast.platform.paas.utils.jsonUtil.JsonCommon;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by zhming on 2015/10/30.
 */

/**
 * 1、 value， method；
 * value：     指定请求的实际地址，指定的地址可以是URI Template 模式（后面将会说明）；
 * method：  指定请求的method类型， GET、POST、PUT、DELETE等；
 * 2、 consumes，produces；
 * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
 * produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
 * 3、 params，headers；
 * params： 指定request中必须包含某些参数值是，才让该方法处理。
 * headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。
 */
@RestController
@RequestMapping(value = "/api/questions")
public class QuestionResource {
    private static final Log log = LogFactory.getLog(QuestionResource.class);
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询所有
     * @param isPage 是否分页
     * @param pageIndex 当前页
     * @param pageSize 每页size
     * @param orderBy 排序
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Object list(
            @RequestParam(value = "isPage", defaultValue = "false", required = false) Boolean isPage,
            @RequestParam(value = "pageIndex", defaultValue = "1", required = false) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "9999", required = false) int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "", required = false) String orderBy
    ){
        try{
            if (isPage) {
                Map map = Maps.newHashMap();
                PageInfo<User> page = userService.page(pageIndex, pageSize, orderBy);
                map.put("pageSize", page.getPageSize());    //每页记录数
                map.put("pageNum", page.getPageNum());      //当前页码
                map.put("recordCount", page.getTotal());    //总记录数
                map.put("pageCount", page.getPages());      //总页数
                map.put("dataList", page.getList());        //数据列表
                //统一格式返回
                return new JsonCommon(Constants.JV_SUCCESS,map);
            } else {
                List<User> userList = userService.getAll();
                //统一格式返回
                return new JsonCommon(Constants.JV_SUCCESS,userList);
            }
        }catch(Exception e){
            log.error(e);
            return new JsonCommon(Constants.JV_ERROR_DATABASE,null);
        }
    }
}