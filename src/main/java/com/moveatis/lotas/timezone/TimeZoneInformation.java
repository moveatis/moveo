package com.moveatis.lotas.timezone;

import java.util.TimeZone;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
public class TimeZoneInformation {
    
    /**
     * We use stardard UTC timezone for saving information to
     * the server - the client can then convert this time
     * his/her timezone
     */
    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");
    
    public TimeZoneInformation() {
        
    }

    public static TimeZone getTimeZone() {
        return TIMEZONE;
    }
}
