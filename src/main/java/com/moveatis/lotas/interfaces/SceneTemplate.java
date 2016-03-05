package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.scene.SceneGroupEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(SceneTemplate.class)
public interface SceneTemplate {

    void create(SceneGroupEntity sceneTemplateEntity);

    void edit(SceneGroupEntity sceneTemplateEntity);

    void remove(SceneGroupEntity sceneTemplateEntity);

    SceneGroupEntity find(Object id);

    List<SceneGroupEntity> findAll();

    List<SceneGroupEntity> findRange(int[] range);

    int count();
    
}
