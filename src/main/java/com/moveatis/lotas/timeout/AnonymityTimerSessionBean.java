package com.moveatis.lotas.timeout;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sami Kallio <phinalium at outlook.com>
 */
@Stateless
@LocalBean
public class AnonymityTimerSessionBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymityTimerSessionBean.class);
    //private static final long TIME_IN_MILLISECONDS = 2*60*60*1000; //two hours
    private static final long TIME_IN_MILLISECONDS = 5000;
    
    @Resource
    private TimerService timerService;
    
    public void setTimer() {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("Single action timer for erasing anonymity information from database");
        timerService.createSingleActionTimer(TIME_IN_MILLISECONDS, timerConfig);
    }
    
    @Timeout
    public void timeUp(Timer timer) {
        LOGGER.debug("Timeout!");
    }
    
}
