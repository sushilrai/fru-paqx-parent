/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public interface Defineable
{
    String getProductFamily();

    void setProductFamily(String productFamily);

    String getProduct();

    void setProduct(String product);

    String getModelFamily();

    void setModelFamily(String modelFamily);

    String getModel();

    void setModel(String model);
}
