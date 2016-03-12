package com.moveatis.lotas.restful;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@ApplicationPath("/webapi")
public class MoveatisRestApplication extends Application {

    public MoveatisRestApplication() {
        
    }
    
    
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(RecordListenerBean.class);
        return classes;
    }
    
    @GET
    public String nothingToSeeHere() {
        return "Nothing to see here!";
    }
}
