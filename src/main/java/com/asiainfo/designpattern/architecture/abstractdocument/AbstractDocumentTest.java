package com.asiainfo.designpattern.architecture.abstractdocument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年4月25日  下午6:53:09
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class AbstractDocumentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocumentTest.class);
    
    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        LOGGER.info("Constructing parts and car");

        // 主属性
        Map<String, Object> carProperties = new HashMap<>();
        carProperties.put(HasModel.PROPERTY, "300SL");
        carProperties.put(HasPrice.PROPERTY, 10000L);

        // 子属性
        Map<String, Object> wheelProperties = new HashMap<>();
        wheelProperties.put(HasType.PROPERTY, "wheel");
        wheelProperties.put(HasModel.PROPERTY, "15C");
        wheelProperties.put(HasPrice.PROPERTY, 100L);

        // 子属性
        Map<String, Object> doorProperties = new HashMap<>();
        doorProperties.put(HasType.PROPERTY, "door");
        doorProperties.put(HasModel.PROPERTY, "Lambo");
        doorProperties.put(HasPrice.PROPERTY, 300L);

        // 添加多个子属性
        carProperties.put(HasParts.PROPERTY, Arrays.asList(wheelProperties, doorProperties));

        Car car = new Car(carProperties);

        LOGGER.info("Here is our car:");
        LOGGER.info("-> model: {}", car.getModel().get());
        LOGGER.info("-> price: {}", car.getPrice().get());
        LOGGER.info("-> parts: ");
        car.getParts().forEach(p -> LOGGER.info("\t{}/{}/{}", p.getType().get(), p.getModel().get(), p.getPrice().get()));
    }
}
