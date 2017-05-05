package com.batsw.anonimitychat.persistence.util;

import java.util.List;

/**
 * Created by tudor on 5/4/2017.
 */

public interface IEntityDbOperations {
    public List<IDbEntity> getAllIDbEntity();

    public IDbEntity getIDbEntityById(long sessionId);

    public boolean addDbEntity(IDbEntity dbEntity);

    public int updateDbEntity(IDbEntity dbEntity);

    public boolean deleteDbEntity(IDbEntity dbEntity);
}
