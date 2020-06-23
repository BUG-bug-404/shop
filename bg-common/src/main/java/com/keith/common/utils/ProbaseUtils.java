package com.keith.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProbaseUtils {
    private static Logger log = LoggerFactory.getLogger(ProbaseUtils.class);
    private static ProbaseUtils configProperties = null;
    private Properties properties;
    private static final String PROP_PATH = "/probase.properties";

    private ProbaseUtils() {
        properties = null;
        init();
    }

    private InputStream getInputStream() {
        return this.getClass().getResourceAsStream(PROP_PATH);
    }

    private void init() {
        if (properties == null)
            properties = new Properties();
        try {
            properties.load(getInputStream());
        } catch (IOException e) {
            log.error("未找到配置文件[config.properties]", e);
        }
    }

    public static ProbaseUtils getInstance() {
        if (configProperties == null)
            configProperties = new ProbaseUtils();
        return configProperties;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
    

}
