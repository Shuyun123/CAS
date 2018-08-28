package net.anumbrella.sso.controller;

import org.apereo.cas.services.RegexRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ReturnAllAttributeReleasePolicy;
import org.apereo.cas.services.ServicesManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;

/**
 * @author anumbrella
 */
@RestController
@RequestMapping("/services")
public class ServicesManagerController {


    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;


    /**
     * 添加cas客户端
     * 增加了单点退出功能，cas退出默认使用隐式退出
     * protocol 代表的是协议，比如: http或者https的协议
     */
    @RequestMapping(value = "/addClient/{protocol}/{serviceId}/{id}", method = RequestMethod.GET)
    public String addService(@PathVariable("serviceId") String serviceId, @PathVariable("protocol") String protocol
            , @PathVariable("id") int id) throws IOException {
        String url = protocol + "://" + serviceId;
        RegisteredService svc = servicesManager.findServiceBy(url);
        if (svc != null) {
            return "0";
        }
        //serviceId,可以配置为正则匹配
        String a = "^" + url + ".*";
        RegexRegisteredService service = new RegexRegisteredService();
        ReturnAllAttributeReleasePolicy re = new ReturnAllAttributeReleasePolicy();
        service.setServiceId(a);
        service.setId(id);
        service.setAttributeReleasePolicy(re);
        //将name统一设置为servicesId
        service.setName(serviceId);
        //单点登出
        service.setLogoutUrl(new URL(url));
        servicesManager.save(service, true);
        servicesManager.load();
        return "1";
    }

    /**
     * 删除服务
     *
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/delete/{serviceId}", method = RequestMethod.GET)
    public String delService(@PathVariable("serviceId") String serviceId) {
        String res = "";
        RegisteredService svc = servicesManager.findServiceBy(serviceId);
        if (svc != null) {
            try {
                servicesManager.delete(svc);
            } catch (Exception e) {
                if (null == servicesManager.findServiceBy(serviceId)) {
                    res = "success";
                    servicesManager.load();
                } else {
                    res = "failed";
                }
            }
        }
        return res;
    }
}