package com.moveatis.lotas.facade;

import com.moveatis.lotas.scene.SceneEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface SceneFacadeLocal {

    void create(SceneEntity sceneEntity);

    void edit(SceneEntity sceneEntity);

    void remove(SceneEntity sceneEntity);

    SceneEntity find(Object id);

    List<SceneEntity> findAll();

    List<SceneEntity> findRange(int[] range);

    int count();
    
}
