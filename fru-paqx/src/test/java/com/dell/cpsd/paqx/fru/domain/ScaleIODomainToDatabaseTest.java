package com.dell.cpsd.paqx.fru.domain;

import com.dell.cpsd.paqx.fru.amqp.config.ConsumerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ContextConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistenceConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PersistencePropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProducerConfig;
import com.dell.cpsd.paqx.fru.amqp.config.ProductionConfig;
import com.dell.cpsd.paqx.fru.amqp.config.PropertiesConfig;
import com.dell.cpsd.paqx.fru.amqp.config.RabbitConfig;
import org.h2.tools.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by kenefj on 03/05/17.
 */
@RunWith(SpringRunner.class)
@Import({ConsumerConfig.class, ContextConfig.class, PersistenceConfig.class, PersistencePropertiesConfig.class, ProducerConfig.class, ProductionConfig.class, PropertiesConfig.class, RabbitConfig.class})
@DataJpaTest

public class ScaleIODomainToDatabaseTest
{
    private static int idCount=0;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void test() throws Exception
    {
        Server webServer = Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        Server server = Server.createTcpServer().start();//("-web","-webAllowOthers","-webPort","9092").start();
        ScaleIOData data = new ScaleIOData(newId(), "scaleIODataName", "scaleIODataInstallID", "mdmNode", "systemVersionName", "mdmClusterState", "version");

        List<ScaleIOIP> ipList = new ArrayList<>();
        ScaleIOTiebreakerScaleIOIP ip1=new ScaleIOTiebreakerScaleIOIP(newId(),"1.2.3.4");
        ScaleIOTiebreakerScaleIOIP ip2=new ScaleIOTiebreakerScaleIOIP(newId(),"5.6.7.8");
        ipList.add(ip1);
        ipList.add(ip2);

        ip1.setScaleIOData(data);
        ip2.setScaleIOData(data);
        data.setTiebreakerScaleIOList(ipList);

        List<ScaleIOIP> ipList2 = new ArrayList<>();
        ScaleIOPrimaryMDMIP ip3=new ScaleIOPrimaryMDMIP(newId(),"1.2.3.4");
        ScaleIOPrimaryMDMIP ip4=new ScaleIOPrimaryMDMIP(newId(),"5.6.7.8");
        ipList2.add(ip3);
        ipList2.add(ip4);


        ip3.setScaleIOData(data);
        ip4.setScaleIOData(data);
        data.setPrimaryMDMIPList(ipList2);

        List<ScaleIOIP> ipList3 = new ArrayList<>();
        ScaleIOPrimaryMDMIP ip5=new ScaleIOPrimaryMDMIP(newId(),"1.2.3.4");
        ScaleIOPrimaryMDMIP ip6=new ScaleIOPrimaryMDMIP(newId(),"5.6.7.8");
        ipList3.add(ip5);
        ipList3.add(ip6);


        ip5.setScaleIOData(data);
        ip6.setScaleIOData(data);
        data.setSecondaryMDMIPList(ipList3);

        //Create mdm cluster
        ScaleIOMdmCluster cluster= new ScaleIOMdmCluster(newId(), "clusterName", "clusterState", "clusterMode", 11,12);

        cluster.setScaleIOData(data);
        data.setMdmCluster(cluster);

        //Add the slaveElement
        ScaleIOSlaveElementInfo slave=new ScaleIOSlaveElementInfo(newId(),1234,"version1","slave", "slave-role", "slave-status");
        slave.addIP(new ScaleIOSlaveScaleIOIP(newId(),"10.11.12.13"));
        slave.addIP(new ScaleIOSlaveScaleIOIP(newId(),"11.12.13.14"));
        slave.setMdmCluster(cluster);
        cluster.addSlave(slave);

        //Add the tiebreaker element
        ScaleIOTiebreakerElementInfo tiebreaker=new ScaleIOTiebreakerElementInfo(newId(),1235,"version2","tiebreaker", "tiebreaker-role", "tiebreaker-status");
        tiebreaker.addIP(new ScaleIOTiebreakerScaleIOIP(newId(),"12.13.14.15"));
        tiebreaker.addIP(new ScaleIOTiebreakerScaleIOIP(newId(),"13.14.15.16"));
        tiebreaker.setMdmCluster(cluster);
        cluster.addTiebreaker(tiebreaker);

        //Add the master element
        ScaleIOMasterElementInfo master=new ScaleIOMasterElementInfo(newId(),1236,"version3","master", "master-role");
        master.addIP(new ScaleIOMasterScaleIOIP(newId(),"14.15.16.17"));
        master.addIP(new ScaleIOMasterScaleIOIP(newId(),"15.16.17.18"));
        master.setMdmCluster(cluster);
        cluster.addMaster(master);

        //Add an sdc element.
        for (int i=0; i<4; i++)
        {
            ScaleIOSDC sdc = new ScaleIOSDC(newId(), "sdcName"+i, "192.168.1." + i, "guid"+i, "ok");
            sdc.setScaleIOData(data);
            data.addSdc(sdc);
        }

        ScaleIOFaultSet faultSet = new ScaleIOFaultSet(newId(), "faultSetName");
        ScaleIOProtectionDomain protectionDomain = new ScaleIOProtectionDomain(newId(), "protectionDomainName", "stateActive");
        faultSet.setProtectionDomain(protectionDomain);
        protectionDomain.addFaultSet(faultSet);

        protectionDomain.setScaleIOData(data);
        data.addProtectionDomain(protectionDomain);



        //Add an sds element.
        for (int i=0; i<4; i++)
        {
            ScaleIOSDS sds = new ScaleIOSDS(newId(), "sdsName"+i, "active"+i , 1230+i);
            sds.setScaleIOData(data);
            data.addSds(sds);

            if (i%2 == 0)
            {
                sds.setFaultSet(faultSet);
                faultSet.addSDS(sds);
            }
            sds.setProtectionDomain(protectionDomain);
            protectionDomain.addSDS(sds);

            //Add two ips
            ScaleIORoleIP roleIP1 = new ScaleIORoleIP("role1", "192.168.2."+i);
            roleIP1.setSds(sds);
            sds.addRoleIP(roleIP1);

            ScaleIORoleIP roleIP2 = new ScaleIORoleIP("role1", "192.168.3."+i);
            roleIP2.setSds(sds);
            sds.addRoleIP(roleIP2);
        }



        ScaleIOData data2=testEntityManager.persist(data);

        assertTrue(true);

        ScaleIOData data3=testEntityManager.find(ScaleIOData.class,1l);

        java.lang.System.out.println("Hello");


    }

    private static String newId()
    {
        return new String("id"+(idCount++));
    }
}