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
package org.onosproject.pce.cli;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.incubator.net.tunnel.Tunnel;
import org.onosproject.incubator.net.tunnel.TunnelId;
import org.onosproject.incubator.net.tunnel.TunnelService;
import org.onosproject.pce.pceservice.api.PceService;
import org.onosproject.pce.pcestore.PathReqKey;
import org.onosproject.pce.pcestore.ScheduledPathInfo;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Supports quering PCE scheduled path.
 */
@Command(scope = "onos", name = "pce-query-scheduled-path",
        description = "Supports querying PCE path.")
public class PceQueryScheduledPathCommand extends AbstractShellCommand {
    private final Logger log = getLogger(getClass());
    public static final String COST_TYPE = "costType";

    @Option(name = "-id", aliases = "--id", description = "path-id", required = false,
            multiValued = false)
    String id = null;

    @Override
    protected void execute() {
        log.info("executing pce-query-scheduled-path");

        PceService service = get(PceService.class);
        TunnelService tunnelService = get(TunnelService.class);


        if (null == id) {
            Map<PathReqKey, ScheduledPathInfo> scheduledPathInfoMap = service.getScheduledPaths();
            Collection<Tunnel> tunnels = tunnelService.queryAllTunnels();

            for (Map.Entry<PathReqKey, ScheduledPathInfo> info : scheduledPathInfoMap.entrySet()) {
                for (Tunnel t : tunnels) {
                    if (t.tunnelName().value().equals(info.getKey().name())
                            && t.path().src().deviceId().equals(info.getKey().src())) {
                        display(info.getValue(), t.state().name());
                    }

                }
            }

        } else {

            ScheduledPathInfo scheduledPathInfo = service.queryScheduledPath(TunnelId.valueOf(id));
            display(scheduledPathInfo, tunnelService.queryTunnel(TunnelId.valueOf(id)).state().name());

            if (scheduledPathInfo == null) {
                print("Path doesnot exists.");
                return;
            }
        }
    }

    /**
     * Display scheduled LSP information on the terminal.
     *
     * @param scheduledPathInfo scheduling LSP info
     */
    void display(ScheduledPathInfo scheduledPathInfo, String tunnelState) {
        print("\nstartDate          : %s \n" +
                "lspStatus          : %s_" +
                "%s\n" +
                "lsp duration       : %d \n" +
                "repeat Pattern     : %s \n",
                scheduledPathInfo.startDate().toString(),
                scheduledPathInfo.lspStatus().name(),
                tunnelState,
                scheduledPathInfo.duration(),
                scheduledPathInfo.repeatPattern().name());

        switch (scheduledPathInfo.repeatPattern()) {
            case ONCE:
            case DAILY:
                print("time               : %s \n",
                        scheduledPathInfo.repeatTime().toString());
                break;
            case WEEKLY:
                print("day of week        : %s \n",
                        scheduledPathInfo.repeatWeekDay().name());
                break;
            case MONTHLY:
                print("repeat date        : %d \n",
                        scheduledPathInfo.repeatDate());
                break;
            default:
                error("Repeat Pattern is not valid [Daily/Weekly/Monthly/Once]");
                return;
        }
    }
}
