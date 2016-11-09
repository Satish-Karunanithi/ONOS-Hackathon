/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.pce.pcestore.api;

import org.onosproject.incubator.net.tunnel.TunnelId;
import org.onosproject.net.resource.ResourceConsumer;
import org.onosproject.pce.pcestore.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Abstraction of an entity providing pool of available labels to devices, links and tunnels.
 */
public interface PceStore {
    /**
     * Checks whether tunnel id is present in tunnel info store.
     *
     * @param tunnelId tunnel id
     * @return success of failure
     */
    boolean existsTunnelInfo(TunnelId tunnelId);

    /**
     * Checks whether path info is present in failed path info list.
     *
     * @param failedPathInfo failed path information
     * @return success or failure
     */
    boolean existsFailedPathInfo(PcePathInfo failedPathInfo);

    /**
     * Retrieves the tunnel info count.
     *
     * @return tunnel info count
     */
    int getTunnelInfoCount();

    /**
     * Retrieves the failed path info count.
     *
     * @return failed path info count
     */
    int getFailedPathInfoCount();

    /**
     * Retrieves tunnel id and pcecc tunnel info pairs collection from tunnel info store.
     *
     * @return collection of tunnel id and resource consumer pairs
     */
    Map<TunnelId, ResourceConsumer> getTunnelInfos();

    /**
     * Retrieves path info collection from failed path info store.
     *
     * @return collection of failed path info
     */
    Iterable<PcePathInfo> getFailedPathInfos();

    /**
     * Retrieves local label info with tunnel consumer id from tunnel info store.
     *
     * @param tunnelId tunnel id
     * @return resource consumer
     */
    ResourceConsumer getTunnelInfo(TunnelId tunnelId);

    /**
     * Stores local label info with tunnel consumer id into tunnel info store for specified tunnel id.
     *
     * @param tunnelId tunnel id
     * @param tunnelConsumerId tunnel consumer id
     */
    void addTunnelInfo(TunnelId tunnelId, ResourceConsumer tunnelConsumerId);

    /**
     * Stores path information into failed path info store.
     *
     * @param failedPathInfo failed path information
     */
    void addFailedPathInfo(PcePathInfo failedPathInfo);

    /**
     * Removes local label info with tunnel consumer id from tunnel info store for specified tunnel id.
     *
     * @param tunnelId tunnel id
     * @return success or failure
     */
    boolean removeTunnelInfo(TunnelId tunnelId);

    /**
     * Removes path info from failed path info store.
     *
     * @param failedPathInfo failed path information
     * @return success or failure
     */
    boolean removeFailedPathInfo(PcePathInfo failedPathInfo);

    /**
     * Stores scheduled path information into scheduled path info store.
     *
     * @param pathReqKey source and tunnel name as a path request key
     * @param ScheduledPathInfo scheduled path info
     */
    void addScheduledPath(PathReqKey pathReqKey, ScheduledPathInfo ScheduledPathInfo);

    /**
     * Get all scheduled path info.
     *
     * @return all scheduled path info
     */
    Map<PathReqKey, ScheduledPathInfo> getScheduledPaths();

    /**
     * Get scheduled path info based on path request key.
     *
     * @param pathReqKey source and tunnel name as a path request key
     * @return scheduled path info
     */
    ScheduledPathInfo getScheduledPath(PathReqKey pathReqKey);

    /**
     * Get all scheduled path info from scheduled path store.
     *
     * @return all scheduled path info
     */
    Collection<ScheduledPathInfo> getAllScheduledPath();

    /**
     * Removes path info from scheduled path info store.
     *
     * @param pathReqKey source and tunnel name as a path request key
     * @return whether it has removed from store
     */
    boolean removeScheduledPath(PathReqKey pathReqKey);

    /**
     * Replaces scheduled path info based on path request key.
     *
     * @param pathReqKey source and tunnel name as a path request key
     * @param scheduledPathInfo scheduled path info
     */
    void updateScheduledPath(PathReqKey pathReqKey, ScheduledPathInfo scheduledPathInfo);

    /**
     * Stores scheduled path timer into the store.
     *
     * @param pathReqKey source and tunnel name as a path request key
     * @param scheduledLspTimerObj scheduled path timer object
     */
    void addScheduledLspTimer(PathReqKey pathReqKey, ScheduledExecutorService scheduledLspTimerObj);

    /**
     * Removes scheduled path timer.
     *
     * @param pathReqKey source and tunnel name as a path request key
     */
    void removeScheduledLspTimer(PathReqKey pathReqKey);

    /**
     * Stores scheduled path deletion timer into the store.
     *
     * @param key source and tunnel name as a path request key
     * @param scheduledLspDeletionTimerObj scheduled path deletion timer
     */
    void addScheduledLspDeletionTimer(PathReqKey key, ScheduledExecutorService scheduledLspDeletionTimerObj);

    /**
     * Removes scheduled path deletion timer.
     *
     * @param key source and tunnel name as a path request key
     */
    void removeScheduledLspDeletionTimer(PathReqKey key);

    /**
     * Gets scheduled path timer for the specified key.
     *
     * @param key source and tunnel name as a path request key
     * @return scheduled path timer
     */
    ScheduledExecutorService scheduledLspTimerObj(PathReqKey key);

    /**
     * Gets scheduled path deletion timer for the specified key.
     *
     * @param key source and tunnel name as a path request key
     * @return scheduled path deletion timer
     */
    ScheduledExecutorService scheduledLspDeletionTimerObj(PathReqKey key);
}
