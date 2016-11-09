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

import static org.slf4j.LoggerFactory.getLogger;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;

import org.onlab.util.DataRateUnit;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.incubator.net.tunnel.Tunnel;
import org.onosproject.incubator.net.tunnel.TunnelService;
import org.onosproject.net.DeviceId;
import org.onosproject.net.intent.constraint.BandwidthConstraint;
import org.onosproject.net.intent.Constraint;
import org.onosproject.pce.pceservice.constraint.CostConstraint;
import org.onosproject.pce.pceservice.LspType;
import org.onosproject.pce.pceservice.api.PceService;

import org.onosproject.pce.pcestore.RepeatPattern;
import org.slf4j.Logger;

/**
 * Supports creating the pce scheduled path.
 */
@Command(scope = "onos", name = "pce-setup-scheduled-path", description = "Supports creating pce calender path.")
public class PceSetupScheduledPathCommand extends AbstractShellCommand {
    private final Logger log = getLogger(getClass());

    @Argument(index = 0, name = "src", description = "source device.", required = true, multiValued = false)
    String src = null;

    @Argument(index = 1, name = "dst", description = "destination device.", required = true, multiValued = false)
    String dst = null;

    @Argument(index = 2, name = "type", description = "LSP type:" + " It includes "
            + "PCE tunnel with signalling in network (0), "
            + "PCE tunnel without signalling in network with segment routing (1), "
            + "PCE tunnel without signalling in network (2).",
            required = true, multiValued = false)
    int type = 0;

    @Argument(index = 3, name = "name", description = "symbolic-path-name.", required = true, multiValued = false)
    String name = null;

    @Argument(index = 4, name = "startdate", description = "start-date-of-lsp."
            + "Enter in dd-mm-yyyy", required = true, multiValued = false)
    String startDate = null;

    @Argument(index = 5, name = "duaration", description = "duration of lsp in minutes",
            required = true, multiValued = false)
    long duration;

    @Option(name = "-c", aliases = "--cost", description = "The cost attribute IGP cost(1) or TE cost(2)",
            required = false, multiValued = false)
    int cost = 2;

    @Option(name = "-b", aliases = "--bandwidth", description = "The bandwidth attribute of path. "
            + "Data rate unit is in BPS.", required = false, multiValued = false)
    double bandwidth = 0.0;

    @Option(name = "-d", aliases = "--daily", description = "Schedules the lsp daily. "
            + "Enter in hr:min format",
            required = false, multiValued = false)
    String dailyTime;

    @Option(name = "-w", aliases = "--weekly", description = "Schedules the lsp weekly. "
            + "Enter in day[1 to 7] format",
            required = false, multiValued = false)
    byte weekDay;

    @Option(name = "-m", aliases = "--monthly", description = "Schedules the lsp monthly. "
            + "Enter in Date[day] format",
            required = false, multiValued = false)
    byte dayOfmonth;

    @Option(name = "-o", aliases = "--once", description = "Schedules the lsp once. "
            + "Enter in hr:min format",
            required = false, multiValued = false)
    String time;

    @Override
    protected void execute() {
        log.info("executing pce-setup-path");

        PceService service = get(PceService.class);
        TunnelService tunnelService = get(TunnelService.class);

        DeviceId srcDevice = DeviceId.deviceId(src);
        DeviceId dstDevice = DeviceId.deviceId(dst);
        List<Constraint> listConstrnt = new LinkedList<>();

        // LSP type validation
        if ((type < 0) || (type > 2)) {
           error("The LSP type value can be PCE tunnel with signalling in network (0), " +
                 "PCE tunnel without signalling in network with segment routing (1), " +
                 "PCE tunnel without signalling in network (2).");
           return;
        }
        LspType lspType = LspType.values()[type];

        //Validating tunnel name, duplicated tunnel names not allowed
        Collection<Tunnel> existingTunnels = tunnelService.queryTunnel(Tunnel.Type.MPLS);
        for (Tunnel t : existingTunnels) {
            if (t.tunnelName().toString().equals(name)) {
                error("Path creation failed, Tunnel name already exists");
                return;
            }
        }

        // Add bandwidth
        // bandwidth default data rate unit is in BPS
        if (bandwidth != 0.0) {
            listConstrnt.add(BandwidthConstraint.of(bandwidth, DataRateUnit.valueOf("BPS")));
        }

        // Add cost
        // Cost validation
        if ((cost < 1) || (cost > 2)) {
            error("The cost attribute value either IGP cost(1) or TE cost(2).");
            return;
        }
        // Here 'cost - 1' indicates the index of enum
        CostConstraint.Type costType = CostConstraint.Type.values()[cost - 1];
        listConstrnt.add(CostConstraint.of(costType));

        LocalDate startDateInLocalDateFmt = null;
        if (startDate == null) {
            error("Please provide Startdate for LSP scheduling");
            return;
        }

        String[] startDatePattern = startDate.split("-");
        if (startDatePattern[0] == null || startDatePattern[1] == null || startDatePattern[2] == null) {
            error("Please provide Startdate in dd-mm-yyyy format for LSP scheduling");
            return;
        }

        startDateInLocalDateFmt = LocalDate.of(Integer.valueOf(startDatePattern[2]).intValue(),
               Integer.valueOf(startDatePattern[1]).intValue(),
               Integer.valueOf(startDatePattern[0]).intValue());

        if (duration == 0) {
            error("Please provide valid duration for LSP");
            return;
        }

        LocalTime dailyTimeInLocalTimeFmt = null;
        RepeatPattern repeatPattern = null;
        if (dailyTime != null) {
            repeatPattern = RepeatPattern.DAILY;
            String[] hrAndMin = dailyTime.split(":");
            if (hrAndMin[0] == null || hrAndMin[1] == null) {
                error("Please provide valid time [Hr:Min]");
                return;
            }

            dailyTimeInLocalTimeFmt = LocalTime.of(Integer.valueOf(hrAndMin[0]).intValue(), Integer.valueOf(hrAndMin[1]).intValue());
        }

        LocalTime timeInLocalTimeFmt = null;
        if (time != null) {
            repeatPattern = RepeatPattern.ONCE;
            String[] hrAndMin = time.split(":");
            if (hrAndMin[0] == null || hrAndMin[1] == null) {
                error("Please provide valid time [Hr:Min]");
                return;
            }

            timeInLocalTimeFmt = LocalTime.of(Integer.valueOf(hrAndMin[0]).intValue(), Integer.valueOf(hrAndMin[1]).intValue());
        }

        DayOfWeek dayOfWeek = null;
        if (weekDay >= 1 && weekDay <= 7) {
            repeatPattern = RepeatPattern.WEEKLY;
            dayOfWeek = DayOfWeek.of(weekDay);
        }

        if (dayOfmonth >= 1 && dayOfmonth <= 31) {
            repeatPattern = RepeatPattern.MONTHLY;
        }

        if (repeatPattern == null) {
            error("Please provide repeat patten for scheduling [Daily/Weekly/Monthly/Once]");
            return;
        }

        LocalTime repeatTime = repeatPattern.equals(RepeatPattern.ONCE) ? timeInLocalTimeFmt : dailyTimeInLocalTimeFmt;

        switch (repeatPattern) {
            case ONCE:
            case DAILY:
                service.schedulePath(startDateInLocalDateFmt, repeatPattern, repeatTime,
                        duration, srcDevice, dstDevice, name, listConstrnt, lspType);
                break;
            case WEEKLY:
                service.schedulePath(startDateInLocalDateFmt, repeatPattern, dayOfWeek,
                        duration, srcDevice, dstDevice, name, listConstrnt, lspType);
                break;
            case MONTHLY:
                service.schedulePath(startDateInLocalDateFmt, repeatPattern, dayOfmonth,
                        duration, srcDevice, dstDevice, name, listConstrnt, lspType);
                break;
            default:
                error("Repeat Pattern is not valid [Daily/Weekly/Monthly/Once]");
                return;
        }
    }
}
