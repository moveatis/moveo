package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.category.CategoryEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Category.class)
public interface Category {

    void create(CategoryEntity categoryEntity);

    void edit(CategoryEntity categoryEntity);

    void remove(CategoryEntity categoryEntity);

    CategoryEntity find(Object id);
    
    CategoryEntity find(String label);

    List<CategoryEntity> findAll();

    List<CategoryEntity> findRange(int[] range);

    int count();
    
}
