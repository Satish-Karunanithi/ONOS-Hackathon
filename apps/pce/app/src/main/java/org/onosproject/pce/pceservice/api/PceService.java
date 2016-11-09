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
package org.onosproject.pce.pceservice.api;

import org.onosproject.incubator.net.tunnel.Tunnel;
import org.onosproject.incubator.net.tunnel.TunnelId;
import org.onosproject.net.DeviceId;
import org.onosproject.net.intent.Constraint;
import org.onosproject.pce.pceservice.LspType;
import org.onosproject.pce.pcestore.PathReqKey;
import org.onosproject.pce.pcestore.RepeatPattern;
import org.onosproject.pce.pcestore.ScheduledPathInfo;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service to compute path based on constraints, release path,
 * update path with new constraints and query existing tunnels.
 */
public interface PceService {

    /**
     * Creates new path based on constraints and LSP type.
     *
     * @param src source device
     * @param dst destination device
     * @param tunnelName name of the tunnel
     * @param constraints list of constraints to be applied on path
     * @param lspType type of path to be setup
     * @return false on failure and true on successful path creation
     */
    boolean setupPath(DeviceId src, DeviceId dst, String tunnelName, List<Constraint> constraints, LspType lspType);

    /**
     * Updates an existing path.
     *
     * @param tunnelId tunnel identifier
     * @param constraints list of constraints to be applied on path
     * @return false on failure and true on successful path update
     */
    boolean updatePath(TunnelId tunnelId, List<Constraint> constraints);

    /**
     * Releases an existing path.
     *
     * @param tunnelId tunnel identifier
     * @return false on failure and true on successful path removal
     */
    boolean releasePath(TunnelId tunnelId);

    /**
     * Queries all paths.
     *
     * @return iterable of existing tunnels
     */
    Iterable<Tunnel> queryAllPath();

    /**
     * Queries particular path based on tunnel identifier.
     *
     * @param tunnelId tunnel identifier
     * @return tunnel if path exists, otherwise null
     */
    Tunnel queryPath(TunnelId tunnelId);

    /**
     * Sets up pce path based on calendering for daily/once.
     *
     * @param startDate start date of the LSP to be schedule first time
     * @param repeatPattern scheduling pattern [Daily/Weekly/Monthly/Once]
     * @param repeatTime time at which the daily/once scheduling should occur
     * @param duration duration in hours after which the scheduled path should be torn down
     * @param srcDevice source device
     * @param dstDevice destination device
     * @param name name of the tunnel
     * @param listConstrnt list of constraints to be applied on path
     * @param lspType type of path to be setup
     * @throws ParseException while creating tunnel
     */
    void schedulePath(LocalDate startDate, RepeatPattern repeatPattern, LocalTime repeatTime,
                      long duration, DeviceId srcDevice, DeviceId dstDevice, String name, List<Constraint> listConstrnt,
                      LspType lspType);

    /**
     * Sets up pce path based on calendering for weekly.
     *
     * @param startDate start date of the LSP to be schedule first time
     * @param repeatPattern scheduling pattern [Daily/Weekly/Monthly/Once]
     * @param dayOfWeek day of the week on which the weekly scheduling should occur
     * @param duration duration in hours after which the scheduled path should be torn down
     * @param srcDevice source device
     * @param dstDevice destination device
     * @param name name of the tunnel
     * @param listConstrnt list of constraints to be applied on path
     * @param lspType type of path to be setup
     * @throws ParseException while creating tunnel
     */
    void schedulePath(LocalDate startDate, RepeatPattern repeatPattern, DayOfWeek dayOfWeek,
                      long duration, DeviceId srcDevice, DeviceId dstDevice, String name, List<Constraint> listConstrnt,
                      LspType lspType);

    /**
     * Sets up pce path based on calendering for monthly.
     *
     * @param startDate start date of the LSP to be schedule first time
     * @param repeatPattern scheduling pattern [Daily/Weekly/Monthly/Once]
     * @param dayOfmonth date of the month on which the monthly scheduling should occur
     * @param duration duration in hours after which the scheduled path should be torn down
     * @param srcDevice source device
     * @param dstDevice destination device
     * @param name name of the tunnel
     * @param listConstrnt list of constraints to be applied on path
     * @param lspType type of path to be setup
     * @throws ParseException while creating tunnel
     */
    void schedulePath(LocalDate startDate, RepeatPattern repeatPattern, byte dayOfmonth,
                      long duration, DeviceId srcDevice, DeviceId dstDevice, String name, List<Constraint> listConstrnt,
                      LspType lspType);

    /**
     * Query scheduled path information.
     *
     * @param tunnelId tunnel id
     * @return scheduled path information
     */
    ScheduledPathInfo queryScheduledPath(TunnelId tunnelId);

    /**
     * Query all scheduled path information.
     *
     * @return all scheduled path information
     */
    Map<PathReqKey, ScheduledPathInfo> getScheduledPaths();
}