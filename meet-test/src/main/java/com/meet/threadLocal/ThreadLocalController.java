package com.meet.threadLocal;

import com.meet.threadPool.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @program: meet-boot
 * @ClassName ThreadLocal
 * @description:
 * @author: MT
 * @create: 2023-04-03 09:22
 **/
@RestController
@RequestMapping("threadLocal")
public class ThreadLocalController {

    @Autowired
    private ThreadLocalService threadLocalService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AsyncService asyncService;

    @GetMapping("test")
    public String test(@RequestParam Integer userId, @RequestParam Integer companyId, @RequestParam String name){

        try {
            //填充上下文
            OxyContent oxyContent = ThreadLocalUtil.getContext();
            oxyContent.setCompanyId(companyId);
            oxyContent.setUserId(userId);

            String test = threadLocalService.test(userId, companyId, name);
            return test;
        }finally {
            //清空threadLocal，否则可能造成内存泄漏
            ThreadLocalUtil.remove();
        }

    }

    /**
     * 测试ThreadLocalUtil代替HttpServletRequest，不需要层层传递参数，降低代码耦合
     * @param no
     * @return
     */
    @GetMapping("getByNo")
    public String getByNo(@RequestParam String no){
        //主线程中可以正常获取threadLocal
        OxyContent context = ThreadLocalUtil.getContext();
        Integer companyId = Optional.ofNullable(context.getCompanyId()).map(e -> e).orElse(null);

        //测试调用异步方法时是否可以正常获取threadLocal
        asyncService.testInThreadLocalGetAsy();
        return companyId * 2 + "789";
    }

}