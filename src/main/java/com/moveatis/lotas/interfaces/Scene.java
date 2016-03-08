package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.scene.SceneEntity;
import com.moveatis.lotas.user.UserEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Scene.class)
public interface Scene {

    void create(SceneEntity sceneEntity);

    void edit(SceneEntity sceneEntity);

    void remove(SceneEntity sceneEntity);

    SceneEntity find(Object id);

    List<SceneEntity> findAll();

    List<SceneEntity> findRange(int[] range);
    
    List<String> getCategories();
    
    List<SceneEntity> findScenesForUser(UserEntity user);
    
    SceneEntity getSceneEntity();
    

    int count();
    
}
