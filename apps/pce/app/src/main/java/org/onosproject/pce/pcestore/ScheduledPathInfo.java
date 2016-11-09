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

package org.onosproject.pce.pcestore;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduledPathInfo {

    /**
     * The date when the scheduler should start.
     */
    private LocalDate startDate;

    /**
     * The nature of repetition of scheduling.
     */
    private RepeatPattern repeatPattern;

    /**
     * The time at which the daily/once scheduling should occur.
     */
    private LocalTime repeatTime;

    /**
     * The day of the week on which the weekly scheduling should occur.
     */
    private DayOfWeek repeatWeekDay;

    /**
     * The date of the month on which the monthly scheduling should occur.
     */
    private short repeatDate;

    /**
     * The duration in minutes after which the scheduled path should be torn down.
     */
    private long duration;

    /**
     * Input path information to compute the scheduled path.
     */
    private PcePathInfo pcePathInfo;

    /**
     * The LSP status of the calendering paths.
     */
    private LspStatus lspStatus;

    /**
     * Initializes the member fields.
     *
     * @param startDate date when the scheduler should start
     * @param repeatPattern nature of repetition of scheduling
     * @param repeatTime time at which the daily/once scheduling should occur
     * @param duration duration in minutes after which the scheduled path should be torn down
     * @param pcePathInfo path information to compute the scheduled path
     */
    public ScheduledPathInfo (LocalDate startDate, RepeatPattern repeatPattern, LocalTime repeatTime,
                              long duration, PcePathInfo pcePathInfo) {
        this.startDate = startDate;
        this.repeatPattern = repeatPattern;
        this.repeatTime = repeatTime;
        this.duration = duration;
        this.pcePathInfo = pcePathInfo;
    }

    /**
     * Initializes the member fields.
     *
     * @param startDate date when the scheduler should start
     * @param repeatPattern nature of repetition of scheduling
     * @param repeatWeekDay day of the week on which the weekly scheduling should occur
     * @param duration duration in minutes after which the scheduled path should be torn down
     * @param pcePathInfo path information to compute the scheduled path
     */
    public ScheduledPathInfo (LocalDate startDate, RepeatPattern repeatPattern, DayOfWeek repeatWeekDay,
                              long duration, PcePathInfo pcePathInfo) {
        this.startDate = startDate;
        this.repeatPattern = repeatPattern;
        this.repeatWeekDay = repeatWeekDay;
        this.duration = duration;
        this.pcePathInfo = pcePathInfo;
    }

    /**
     * Initializes the member fields.
     *
     * @param startDate date when the scheduler should start
     * @param repeatPattern nature of repetition of scheduling
     * @param repeatDate date of the month on which the monthly scheduling should occur
     * @param duration duration in minutes after which the scheduled path should be torn down
     * @param pcePathInfo path information to compute the scheduled path
     */
    public ScheduledPathInfo (LocalDate startDate, RepeatPattern repeatPattern, short repeatDate,
                              long duration, PcePathInfo pcePathInfo) {
        this.startDate = startDate;
        this.repeatPattern = repeatPattern;
        this.repeatDate = repeatDate;
        this.duration = duration;
        this.pcePathInfo = pcePathInfo;
    }

    /**
     * Returns day of the week on which the weekly scheduling should occur.
     *
     * @return day of the week on which the weekly scheduling should occur
     */
    public DayOfWeek repeatWeekDay() {
        return repeatWeekDay;
    }

    /**
     * Returns date of the month on which the monthly scheduling should occur.
     *
     * @return date of the month on which the monthly scheduling should occur
     */
    public short repeatDate() {
        return repeatDate;
    }

    /**
     * Returns duration in minutes after which the scheduled path should be torn down.
     *
     * @return duration in minutes after which the scheduled path should be torn down
     */
    public long duration() {
        return duration;
    }

    /**
     * Returns time at which the daily/once scheduling should occur.
     *
     * @return time at which the daily/once scheduling should occur
     */
    public LocalTime repeatTime() {
        return repeatTime;
    }

    /**
     * Returns path information to compute the scheduled path.
     *
     * @return path information to compute the scheduled path
     */
    public PcePathInfo pcePathInfo() {
        return pcePathInfo;
    }

    /**
     * Returns date when the scheduler should start.
     *
     * @return date when the scheduler should start
     */
    public LocalDate startDate() {
        return startDate;
    }

    /**
     * Returns nature of repetition of scheduling.
     *
     * @return nature of repetition of scheduling
     */
    public RepeatPattern repeatPattern() {
        return repeatPattern;
    }

    /**
     * Sets LSP status of the path.
     *
     * @param lspStatus LSP status of the path
     */
    public void setLspStatus(LspStatus lspStatus) {
        this.lspStatus = lspStatus;
    }

    /**
     * Returns LSP status of the path.
     *
     * @return LSP status of the path
     */
    public LspStatus lspStatus() {
        return lspStatus;
    }

}
