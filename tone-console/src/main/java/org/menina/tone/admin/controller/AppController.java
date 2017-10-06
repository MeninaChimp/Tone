package org.menina.tone.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.menina.tone.admin.service.ConfigService;
import org.menina.tone.admin.utils.ResponseUtils;
import org.menina.tone.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by menina on 2017/10/2.
 */
@RestController
@RequestMapping("/app")
public class AppController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> all() {
        try {
            return ResponseUtils.successResult(configService.nodes(Constant.APP_PATH));
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, Object> create(@RequestBody AppForm appForm) {
        try {
            String path = Constant.APP_PATH + "/" + appForm.getName();
            configService.save(path, MAPPER.writeValueAsString(appForm));
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }

        return ResponseUtils.successResult("OK");
    }

    @RequestMapping(value = "/info/{app}", method = RequestMethod.GET)
    public Map<String, Object> create(@PathVariable String app) {
        try {
            String path = Constant.APP_PATH + "/" + app;
            return ResponseUtils.successResult(MAPPER.readValue(configService.getData(path), AppForm.class));
        } catch (Exception e) {
            return ResponseUtils.badResult(e.getMessage());
        }
    }
}
