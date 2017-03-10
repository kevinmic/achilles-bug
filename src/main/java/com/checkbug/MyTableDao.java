package com.checkbug;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import info.archinnov.achilles.generated.ManagerFactory;
import info.archinnov.achilles.generated.manager.MyTable_Manager;

import java.util.List;

public class MyTableDao {

    private final MyTable_Manager myTable_manager;

    public MyTableDao(ManagerFactory managerFactory) {
        myTable_manager = managerFactory.forMyTable();

    }

    public void add(MyTable mytable) {
        myTable_manager.crud().insert(mytable).execute();
    }

    public List<MyTable> findByPartitionId(String pKey) {
        return myTable_manager.dsl().select().allColumns_FromBaseTable().where().myKey().Eq(pKey).getList();
    }

    public MyTable findById(String pKey, String col1, String col2) {
        return myTable_manager.crud().findById(pKey, col1, col2).get();
    }

    public List<MyTable> findByPartitionId_wQueryBuilder_and_boundValues(String pKey) {
        // This works
        RegularStatement query = getQueryBuilderForSelectByPartitionKey(pKey);
        // Notice that I am passing pKey into both the QueryBuilder and the typedQueryForSelect
        return myTable_manager.raw().typedQueryForSelect(query, pKey).getList();
    }

    public List<MyTable> findByPartitionId_wQueryBuilder(String pKey) {
        // This throws an error
        RegularStatement query = getQueryBuilderForSelectByPartitionKey(pKey);
        // Notice that I am binding the pKey in the QueryBuilder but not in the typedQueryForSelect.
        // This will cause a datastax error complaining about unset values
        return myTable_manager.raw().typedQueryForSelect(query).getList();
    }

    private Select.Where getQueryBuilderForSelectByPartitionKey(String pKey) {
        return QueryBuilder.select().from(MyTable.TABLE_NAME).where(QueryBuilder.eq("myKey", pKey));
    }


    public void deleteByPartitionId_usingAchilles(String pKey) {
//        Does Not Compile
//        myTable_manager.dsl().delete().allColumns_FromBaseTable().where().myKey().Eq(pKey).execute();
    }

    public void deleteByPartitionId_usingDatastax(String pKey) {
        myTable_manager.getNativeSession().execute(
                QueryBuilder.delete().from(MyTable.TABLE_NAME).where(QueryBuilder.eq("myKey", pKey))
        );
    }
}
