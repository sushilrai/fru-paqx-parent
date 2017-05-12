package com.dell.cpsd.paqx.fru.domain;

import com.dell.cpsd.paqx.fru.amqp.config.*;
import com.dell.cpsd.paqx.fru.amqp.config.PersistenceConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistencePropertiesConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@Import({ConsumerConfig.class, ContextConfig.class, PersistenceConfig.class, PersistencePropertiesConfig.class, ProducerConfig.class, ProductionConfig.class, PropertiesConfig.class, RabbitConfig.class})
@DataJpaTest
public class VcenterDomainToDatabaseTest
{
    private static int idCount=0;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void test() throws Exception
    {
        // Create a vcenter instance
        VCenter vCenter = new VCenter(newId(), "vc-1");
        testEntityManager.persist(vCenter);

        // Create datacenter and persist entry
        Datacenter dc = new Datacenter(newId(), "dc-1");
        dc.setvCenter(vCenter);
        testEntityManager.persist(dc);

        Datacenter dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dc.equals(dcQuery));
        assertTrue(vCenter.equals(dcQuery.getvCenter()));

        // Create clusters
        Cluster cluster1 = new Cluster(newId(), "domain-01");
        Cluster cluster2 = new Cluster(newId(), "domain-02");
        cluster1.setDatacenter(dc);
        cluster2.setDatacenter(dc);

        testEntityManager.persist(cluster1);
        testEntityManager.persist(cluster2);

        testEntityManager.flush();

        // Update dc with clusters
        List<Cluster> clusterList = Arrays.asList(cluster1, cluster2);
        dc.setClusterList(clusterList);
        testEntityManager.persist(dc);

        // Validate cluster list for dc is as expected
        dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dcQuery.getClusterList().containsAll(clusterList));

        // Validate cluster exists
        Cluster clusterQuery = testEntityManager.find(Cluster.class, 1l);
        assertTrue(clusterQuery.getDatacenter().equals(dc));
        assertTrue(clusterQuery.equals(cluster1));


        clusterQuery = testEntityManager.find(Cluster.class, 2l);
        assertTrue(clusterQuery.getDatacenter().equals(dc));
        assertTrue(clusterQuery.equals(cluster2));

//        dcQuery = testEntityManager.find(Datacenter.class,1l);

        // Add datastores to datacenter
        Datastore ds1 = new Datastore(newId(), "ds-01","VMFS", "/im/a/fake/url1");
        Datastore ds2 = new Datastore(newId(), "ds-02","VMFS", "/im/a/fake/url2");
        Datastore ds3 = new Datastore(newId(), "ds-03","VMFS", "/im/a/fake/url3");
        ds1.setDatacenter(dc);
        ds2.setDatacenter(dc);
        ds3.setDatacenter(dc);

        // Validate no datastores exist within dc
        dcQuery = testEntityManager.find(Datacenter.class,1l);
        assertTrue(dcQuery.getDatastoreList().isEmpty());

        // Update dc with datastores
        List<Datastore> datastoreList = Arrays.asList(ds1, ds2, ds3);
        dc.setDatastoreList(datastoreList);
        testEntityManager.persist(dc);

        Datastore datastoreQuery = testEntityManager.find(Datastore.class, 1l);
        assertTrue(datastoreQuery.getDatacenter().equals(dc));
//
//        // Add hosts to clusters
//        Host host1 = new Host(newId(), "host-01","POWERED_ON", cluster1);
//        Host host2 = new Host(newId(), "host-02","POWERED_ON", cluster1);
//        Host host3 = new Host(newId(), "host-03","POWERED_ON", cluster1);
//        List<Host> cluster1HostList = Arrays.asList(host1, host2, host3);
//
//        cluster1.setHostList(cluster1HostList);
//
//        Host host4 = new Host(newId(), "host-04","POWERED_ON", cluster2);
//        Host host5 = new Host(newId(), "host-05","POWERED_ON", cluster2);
//
//        List<Host> cluster2HostList = Arrays.asList(host4, host5);
//
//        cluster2.setHostList(cluster2HostList);




    }

    private static String newId()
    {
        return new String("id"+(idCount++));
    }
}

