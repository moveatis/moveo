package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.event.EventEntity;
import com.moveatis.lotas.user.UserEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Scene.class)
public interface Scene {

    void create(EventEntity sceneEntity);

    void edit(EventEntity sceneEntity);

    void remove(EventEntity sceneEntity);

    EventEntity find(Object id);

    List<EventEntity> findAll();

    List<EventEntity> findRange(int[] range);
    
    List<String> getCategories();
    
    List<EventEntity> findScenesForUser(UserEntity user);
    
    EventEntity getSceneEntity();
    

    int count();
    
}
