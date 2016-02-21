package com.moveatis.lotas.facade;

import com.moveatis.lotas.category.CategoryTemplateEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local
public interface CategoryTemplateFacadeLocal {

    void create(CategoryTemplateEntity categoryTemplateEntity);

    void edit(CategoryTemplateEntity categoryTemplateEntity);

    void remove(CategoryTemplateEntity categoryTemplateEntity);

    CategoryTemplateEntity find(Object id);

    List<CategoryTemplateEntity> findAll();

    List<CategoryTemplateEntity> findRange(int[] range);

    int count();
    
}
