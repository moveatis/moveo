package com.moveatis.lotas.facade;

import com.moveatis.lotas.category.CategoryEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface CategoryFacadeLocal {

    void create(CategoryEntity categoryEntity);

    void edit(CategoryEntity categoryEntity);

    void remove(CategoryEntity categoryEntity);

    CategoryEntity find(Object id);

    List<CategoryEntity> findAll();

    List<CategoryEntity> findRange(int[] range);

    int count();
    
}
