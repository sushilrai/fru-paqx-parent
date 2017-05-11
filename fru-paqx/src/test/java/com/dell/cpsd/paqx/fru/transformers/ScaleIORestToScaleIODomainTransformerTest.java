package com.dell.cpsd.paqx.fru.transformers;

import com.dell.cpsd.paqx.fru.domain.ScaleIOData;
import com.dell.cpsd.storage.capabilities.api.MdmClusterDataRestRep;
import com.dell.cpsd.storage.capabilities.api.ScaleIOSystemDataRestRep;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kenefj on 11/05/2017.
 */
public class ScaleIORestToScaleIODomainTransformerTest
{
    @Test
    public void testNullCase()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        assertNull(transformer.transform(null));
    }

    @Test
    public void testEmptyRep()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        assertTrue(transformer.transform(rep)!=null);
    }

    @Test
    public void testEmptyMDMCluster()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        rep.setMdmClusterDataRestRep(new MdmClusterDataRestRep());
        transformer.transform(rep);
    }

    @Test
    public void testPrimitiveScaleIOSystemDataTransformation()
    {
        ScaleIORestToScaleIODomainTransformer transformer = new ScaleIORestToScaleIODomainTransformer();
        ScaleIOSystemDataRestRep rep = new ScaleIOSystemDataRestRep();
        rep.setId("id1");
        rep.setInstallId("installid1");
        rep.setMdmClusterState("mdmClusterState1");
        rep.setMdmMode("mode1");
        rep.setName("name1");
        rep.setSystemVersionName("systemversionname1");
        rep.setVersion("version1");

        ScaleIOData data=transformer.transform(rep);
        assertTrue( data!= null);
        assertTrue(data.getId().equals("id1"));
        assertTrue(data.getInstallId().equals("installid1"));
        assertTrue(data.getMdmClusterState().equals("mdmClusterState1"));
        assertTrue(data.getMdmMode().equals("mode1"));
        assertTrue(data.getName().equals("name1"));
        assertTrue(data.getSystemVersionName().equals("systemversionname1"));
        assertTrue(data.getVersion().equals("version1"));
    }
}