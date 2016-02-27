package com.moveatis.lotas.facade;

import com.moveatis.lotas.category.application.ApplicationEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface ApplicationFacadeLocal {

    void create(ApplicationEntity applicationEntity);

    void edit(ApplicationEntity applicationEntity);

    void remove(ApplicationEntity applicationEntity);

    ApplicationEntity find(Object id);

    List<ApplicationEntity> findAll();

    List<ApplicationEntity> findRange(int[] range);

    int count();
    
}
