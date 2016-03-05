package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.category.CategoryGroupEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface CategoryGroup {

    void create(CategoryGroupEntity categoryTemplateEntity);

    void edit(CategoryGroupEntity categoryTemplateEntity);

    void remove(CategoryGroupEntity categoryTemplateEntity);

    CategoryGroupEntity find(Object id);

    List<CategoryGroupEntity> findAll();

    List<CategoryGroupEntity> findRange(int[] range);

    int count();
    
}
