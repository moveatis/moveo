package com.moveatis.lotas.restful;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
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
        classes.add(NotFoundExceptionMapper.class);
        
        System.out.println("SYSTEM INITIALIZED");
        
        return classes;
    }
}
