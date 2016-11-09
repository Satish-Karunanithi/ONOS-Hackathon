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

import com.google.common.base.MoreObjects;
import java.util.Objects;
import org.onosproject.net.DeviceId;

/**
 * Input path key information.
 * This path information will be stored in pce store.
 */
public final class PathReqKey {

    private DeviceId src; // source path

    private String name; // path name

    /**
     * Initialization of member variables.
     *
     * @param src source device id
     * @param name tunnel name
     */
    public PathReqKey(DeviceId src,
                    String name) {
       this.src = src;
       this.name = name;
    }

    /**
     * Initialization for serialization.
     */
    public PathReqKey() {
       this.src = null;
       this.name = null;
    }

    /**
     * Returns source device id.
     *
     * @return source device id
     */
    public DeviceId src() {
       return src;
    }

    /**
     * Sets source device id.
     *
     * @param id source device id
     */
    public void src(DeviceId id) {
        this.src = id;
    }

    /**
     * Returns tunnel name.
     *
     * @return name
     */
    public String name() {
       return name;
    }

    /**
     * Sets tunnel name.
     *
     * @param name tunnel name
     */
    public void name(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PathReqKey) {
            final PathReqKey other = (PathReqKey) obj;
            return Objects.equals(this.src, other.src) &&
                    Objects.equals(this.name, other.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("Source", src)
                .add("Name", name)
                .toString();
    }
}
