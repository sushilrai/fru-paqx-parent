/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@MappedSuperclass
public abstract class AbstractElement implements Persistable
{
    @javax.persistence.Version
    @Column(name = "LOCKING_VERSION", nullable = false)
    protected Long lockingVersion = 0L;

    @Transient
    private boolean persisted = false;

    @PostLoad
    @PostPersist
    public void onSetPersisted()
    {
        this.persisted = true;
    }

    @Override
    public boolean isPersisted()
    {
        return persisted;
    }

    public Long getLockingVersion()
    {
        return lockingVersion;
    }

    public static <D> D find(Collection<D> collection, Predicate<D> predicate)
    {
        if (collection != null)
        {
            Optional<D> exists = collection.stream()
                    .filter(predicate)
                    .findAny();
            if (exists.isPresent())
            {
                return exists.get();
            }
        }
        return null;
    }

    public static <D> boolean exists(Collection<D> collection, Predicate<D> predicate)
    {
        return find(collection, predicate) != null;
    }
}
