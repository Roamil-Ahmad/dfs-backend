package com.dfs.switchgateway.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static ConfigReader instance;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private static Map<String, String> cacheMap = new HashMap<String, String>();

    @Value("${local.property}")
    String localProperty;

    private ConfigReader() {

    }

    public static ConfigReader getInstance() {
        if (instance == null)
            instance = new ConfigReader();
        return instance;
    }

    public String getProperty(final String key, final String defaultValue, boolean cache) {
        String result = null;
        if (cache) {
            if (cacheMap.containsKey(key)) {
                result = cacheMap.get(key);
            } else {
                result = getProperty(key, defaultValue);
                if (StringUtils.isNotEmpty(result)) {
                    cacheMap.put(key, result);
                }
            }
        } else {
            result = getProperty(key, defaultValue);
        }
        return result;
    }

    public String getProperty(final String key, final String defaultValue) {

        InputStream inputStream = null;
        String value = null;
        try {
            // Get the inputStream

            inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
//            inputStream = this.getClass().getClassLoader().getResourceAsStream("application-local.properties");
            properties = new Properties();
            // load the inputStream using the Properties
            properties.load(inputStream);

            value = properties.getProperty(key);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.info("Exception", e);
            }
        }
        if (value == null) {
            return defaultValue;
        }
        return value.trim();
    }

//    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
//        logger.info(ConfigReader.getInstance().getProperty("connection_timeout", "222"));
//        long end = System.currentTimeMillis();
//        System.out.println("Total Time Taken : " + (end - startTime));
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < 65000; i++) {
//            ConfigReader.getInstance().getProperty("connection_timeout", "222");
//        }
//        end = System.currentTimeMillis();
//        System.out.println("Total Time Taken : " + (end - startTime));
//    }
}
