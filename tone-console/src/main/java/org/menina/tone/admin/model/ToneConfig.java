package org.menina.tone.admin.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by menina on 2017/10/1.
 */
@Data
public class ToneConfig {

    private String key;

    private String value;

    private Date updateTime;

    private String updater;

    private List<String> ips;


}
