package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.scene.SceneTemplateEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(SceneTemplate.class)
public interface SceneTemplate {

    void create(SceneTemplateEntity sceneTemplateEntity);

    void edit(SceneTemplateEntity sceneTemplateEntity);

    void remove(SceneTemplateEntity sceneTemplateEntity);

    SceneTemplateEntity find(Object id);

    List<SceneTemplateEntity> findAll();

    List<SceneTemplateEntity> findRange(int[] range);

    int count();
    
}
