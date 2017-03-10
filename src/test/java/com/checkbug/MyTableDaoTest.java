package com.checkbug;

import info.archinnov.achilles.generated.ManagerFactory;
import info.archinnov.achilles.generated.ManagerFactoryBuilder;
import info.archinnov.achilles.junit.AchillesTestResource;
import info.archinnov.achilles.junit.AchillesTestResourceBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyTableDaoTest {
    private static final String KEYSPACE_NAME = "keyspace_name";
    public static final String PK_1 = "PK_1";

    @Rule
    public AchillesTestResource<ManagerFactory> resource = AchillesTestResourceBuilder
            .forJunit()
            .createAndUseKeyspace(KEYSPACE_NAME)
            .entityClassesToTruncate(MyTable.class)
            .truncateBeforeAndAfterTest()
            .build((cluster, statementsCache) -> ManagerFactoryBuilder
                    .builder(cluster)
                    .withStatementsCache(statementsCache)
                    .doForceSchemaCreation(true)
                    .withDefaultKeyspaceName(KEYSPACE_NAME)
                    .build()
            );
    private MyTableDao myTableDao;

    @Before
    public void before() {
        myTableDao = new MyTableDao(resource.getManagerFactory());
    }

    @Test
    public void test_nativeQuery_boundValues_problem() {
        MyTable myTable = getMyTable();
        myTableDao.add(myTable);

        List<MyTable> list = myTableDao.findByPartitionId(PK_1);
        System.out.println("List by partition key using dsl - " + list.size());

        list = myTableDao.findByPartitionId_wQueryBuilder_and_boundValues(PK_1);
        System.out.println("List by partition key using native query and manually bound values - " + list.size());

        System.out.println("findByPartitionId_wQueryBuilder will throw exception");
        list = myTableDao.findByPartitionId_wQueryBuilder(PK_1);
    }

    @Test
    public void test_showDeleteIssue() {
        // This one doesn't do anything because achilles wont let me delete using partial keys
        myTableDao.deleteByPartitionId_usingAchilles(PK_1);
        // This one does the same delete but using datastax only
        myTableDao.deleteByPartitionId_usingDatastax(PK_1);
    }

    private MyTable getMyTable() {
        MyTable myTable = new MyTable();
        myTable.setMyKey(PK_1);
        myTable.setMyCCol1("CCOL_1");
        myTable.setMyCCol2("CCOL_2");
        myTable.setMyvar("MyVar");
        return myTable;
    }
}
