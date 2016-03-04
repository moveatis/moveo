package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.application.ApplicationEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Application.class)
public interface Application {

    void create(ApplicationEntity applicationEntity);

    void edit(ApplicationEntity applicationEntity);

    void remove(ApplicationEntity applicationEntity);

    ApplicationEntity find(Object id);

    List<ApplicationEntity> findAll();

    List<ApplicationEntity> findRange(int[] range);

    int count();
    
}
