package com.moveatis.lotas.timers;

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
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Stateless
@LocalBean
public class EventKeyTimerSessionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventKeyTimerSessionBean.class);
    //private static final long TIME_IN_MILLISECONDS = 4*60*60*1000; //four hours
    private static final long TIME_IN_MILLISECONDS = 5000;
    
    @Resource
    private TimerService timerService;
    
    public void setTimer() {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("Single action timer for erasing eventkey based information from database");
        timerService.createSingleActionTimer(TIME_IN_MILLISECONDS, timerConfig);
    }
    
    @Timeout
    public void timeUp(Timer timer) {
        LOGGER.debug("Timeout!");
    }
    
}
