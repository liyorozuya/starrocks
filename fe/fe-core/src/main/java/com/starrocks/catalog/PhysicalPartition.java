// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.starrocks.catalog;

import com.starrocks.catalog.MaterializedIndex.IndexExtState;
import com.starrocks.transaction.TransactionType;

import java.util.List;

/* 
 * PhysicalPartition is the interface that describes the physical storage of a partition.
 * It includes version information and one or more MaterializedIndexes.
 * Each MaterializedIndex contains multiple tablets.
 */
public interface PhysicalPartition {

    // partition id which contains this physical partition
    public long getParentId();
    public void setParentId(long parentId);

    // physical partition id
    public long getId();
    public String getName();
    public void setName(String name);
    public void setIdForRestore(long id);
    public long getBeforeRestoreId();

    public long getShardGroupId();

    public void setShardGroupId(long shardGroupId);

    public List<Long> getShardGroupIds();

    public void setImmutable(boolean isImmutable);
    public boolean isImmutable();

    // version interface

    public void updateVersionForRestore(long visibleVersion);

    public void updateVisibleVersion(long visibleVersion);

    public void updateVisibleVersion(long visibleVersion, long visibleVersionTime);
    public void updateVisibleVersion(long visibleVersion, long visibleVersionTime, long visibleTxnId);
    public long getVisibleVersion();
    public long getVisibleVersionTime();
    public void setVisibleVersion(long visibleVersion, long visibleVersionTime);
    public long getNextVersion();
    public void setNextVersion(long nextVersion);
    public long getCommittedVersion();
    public long getDataVersion();
    public void setDataVersion(long dataVersion);
    public long getNextDataVersion();
    public void setNextDataVersion(long nextDataVersion);
    public long getCommittedDataVersion();
    public long getVersionEpoch();
    public void setVersionEpoch(long versionEpoch);
    public long nextVersionEpoch();
    public TransactionType getVersionTxnType();
    public void setVersionTxnType(TransactionType versionTxnType);
    public long getVisibleTxnId();

    // materialized index interface

    public void createRollupIndex(MaterializedIndex mIndex);
    public MaterializedIndex deleteRollupIndex(long indexId);
    public void setBaseIndex(MaterializedIndex baseIndex);
    public MaterializedIndex getBaseIndex();
    public MaterializedIndex getIndex(long indexId);
    public List<MaterializedIndex> getMaterializedIndices(IndexExtState extState);
    /*
     * Change the index' state from SHADOW to NORMAL
     */
    public boolean visualiseShadowIndex(long shadowIndexId, boolean isBaseIndex);

    // statistic interface

    // max data size of one tablet in this physical partition
    public long getTabletMaxDataSize();
    // partition data size reported by be, but may be not accurate
    public long storageDataSize();
    // partition row count reported by be, but may be not accurate
    public long storageRowCount();
    // partition replica count, it's accurate 
    public long storageReplicaCount();
    // has data judge by fe version, it's accurate
    public boolean hasStorageData();
    public boolean hasMaterializedView();
    public boolean isFirstLoad();

    // for lake partition
    public long getMinRetainVersion();
    public void setMinRetainVersion(long minRetainVersion);
    public long getLastVacuumTime();
    public void setLastVacuumTime(long lastVacuumTime);

}
