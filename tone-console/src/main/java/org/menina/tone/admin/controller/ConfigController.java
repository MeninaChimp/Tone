package org.menina.tone.admin.controller;

import org.joda.time.DateTime;
import org.menina.tone.admin.model.ToneConfig;
import org.menina.tone.admin.service.ConfigService;
import org.menina.tone.admin.utils.ResponseUtils;
import org.menina.tone.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Menina on 2017/9/23.
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

//    @RequestMapping(value = "/active", method = RequestMethod.POST)
//    public Map<String, Object> activeZookeeperConfig(@RequestBody ConfigurationForm dto) {
//        try {
//            configService.initialZookeeperClient(dto.getRegistryCenterAddress());
//        } catch (Exception e) {
//            return ResponseUtils.badResult(e.getMessage());
//        }
//
//        return ResponseUtils.successResult("OK");
//    }

    @RequestMapping(value = "/keys/{parent}", method = RequestMethod.GET)
    public Map<String, Object> getZookeeperConfig(@PathVariable String parent) {
        try {
            return ResponseUtils.successResult(configService.nodes(parent));
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/detail/{app}", method = RequestMethod.GET)
    public Map<String, Object> details(@PathVariable String app) {
        try {
            String path = Constant.APP_PATH + Constant.SEPARATOR + app;
            String grayIp = Constant.GRAY_SCALA_APP_PATH + Constant.SEPARATOR + app;
            List<String> nodes = configService.nodes(path);
            List<ToneConfig> configurations = new ArrayList<>();
            for (String node : nodes) {
                ToneConfig config = new ToneConfig();
                String leaf = path + Constant.SEPARATOR + node;
                config.setKey(node);
                config.setValue(this.configService.getData(leaf));
                String updateTime = this.configService.getData(leaf + Constant.UPDATE_TIME);
                config.setUpdateTime(updateTime == null ? null : new DateTime(updateTime).toDate());
                List<String> ips = this.configService.nodes(grayIp);
                config.setIps(ips);
                config.setUpdater(this.configService.getData(leaf + Constant.UPDATE_USER));
                configurations.add(config);
            }

            return ResponseUtils.successResult(configurations);
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Map<String, Object> remove(@RequestBody ConfigForm configForm) {
        try {
            String path = Constant.APP_PATH + Constant.SEPARATOR + configForm.getApp() + Constant.SEPARATOR + configForm.getKey();
            configService.removeNode(path);
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }

        return ResponseUtils.successResult("OK");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, Object> create(WebRequest webRequest, @RequestBody ConfigForm form) {
        try {
            String path = Constant.APP_PATH + Constant.SEPARATOR + form.getApp() + Constant.SEPARATOR + form.getKey();
            configService.save(path, form.getValue());
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }

        return ResponseUtils.successResult("OK");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody ConfigForm form) {
        try {
            String path = Constant.APP_PATH + Constant.SEPARATOR + form.getApp() + Constant.SEPARATOR + form.getKey();
            configService.update(path, form.getValue());
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }

        return ResponseUtils.successResult("OK");
    }

    @RequestMapping(value = "/grayRelease", method = RequestMethod.POST)
    public Map<String, Object> grayRelease(@RequestBody ConfigForm form) {
        try {
            String path = Constant.APP_PATH + Constant.SEPARATOR + form.getApp() + Constant.SEPARATOR + form.getKey()
                    + Constant.IPS + Constant.SEPARATOR + form.getIp() + Constant.SEPARATOR + form.getKey();
            configService.grayRelease(path, form.getValue());
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }

        return ResponseUtils.successResult("OK");
    }
}
