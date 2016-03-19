package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.event.EventGroupEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(SceneTemplate.class)
public interface SceneTemplate {

    void create(EventGroupEntity sceneTemplateEntity);

    void edit(EventGroupEntity sceneTemplateEntity);

    void remove(EventGroupEntity sceneTemplateEntity);

    EventGroupEntity find(Object id);

    List<EventGroupEntity> findAll();

    List<EventGroupEntity> findRange(int[] range);

    int count();
    
}
