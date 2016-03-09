package com.moveatis.lotas.interfaces;

import com.moveatis.lotas.records.RecordEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@Local(Record.class)
public interface Record {

    void create(RecordEntity recordEntity);

    void edit(RecordEntity recordEntity);

    void remove(RecordEntity recordEntity);

    RecordEntity find(Object id);

    List<RecordEntity> findAll();

    List<RecordEntity> findRange(int[] range);

    int count();
    
}
