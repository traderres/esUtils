package com.lessons.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringAppContextUtils implements ApplicationContextAware
{
    private static final Logger logger = LoggerFactory.getLogger(SpringAppContextUtils.class);

    private static ApplicationContext applicationContext = null;



    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext aApplicationContext) throws BeansException
    {
        // Assign the ApplicationContext into a static variable
        applicationContext = aApplicationContext;
    }


    public static Object getBean(String aName)
    {
        if (applicationContext == null)
        {
            throw new RuntimeException("Error in SpringAppContextUtils.getBean().  The applicationContext is null");
        }

        return applicationContext.getBean(aName);
    }

}
