/*
 * Copyright (c) 2014 Goran Ehrsson.
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
 * under the License.
 */

package grails.plugins.sequence

import groovy.transform.CompileStatic
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Created by goran on 2014-06-24.
 */
class RedisSequenceGenerator<T extends Number> implements SequenceGenerator<T> {

    GrailsApplication grailsApplication

    def redisService

    private static final FORMAT_KEY = '='

    @CompileStatic
    private String getKey(long tenant, String name, String group) {
        def s = new StringBuilder()
        s << tenant.toString()
        s << '/'
        s << name
        if (group) {
            s << '/'
            s << group
        }
        s.toString()
    }

    /**
     * Create a new sequence.
     *
     * @param tenant tenant ID
     * @param name sequence name
     * @param group sub-sequence
     * @param format number format
     * @param start start number
     * @return current sequence status
     */
    @Override
    SequenceStatus create(long tenant, String name, String group, String format, T start) {
        if (!format) {
            format = "%d"
        }
        if (start == null) {
            start = 1L
        }
        redisService.withRedis { redis ->
            redis.set(getKey(tenant, name, FORMAT_KEY), format)
            redis.set(getKey(tenant, name, group), start.toString())
        }
        new SequenceStatus(name, format, start)
    }

    /**
     * Delete a sequence.
     *
     * @param tenant tenant ID
     * @param name sequence name
     * @param group sub-sequence
     * @return true if sequence was removed
     */
    @Override
    boolean delete(long tenant, String name, String group) {
        redisService.withRedis { redis ->
            if (redis.get(getKey(tenant, name, group))) {
                redis.del(getKey(tenant, name, FORMAT_KEY))
                redis.del(getKey(tenant, name, group))
                return true
            }
            return false
        }
    }

    /**
     * Get next unique number formatted.
     *
     * @param tenant tenant ID
     * @param name sequence name
     * @param group sub-sequence
     * @return formatted number
     */
    @Override
    String nextNumber(long tenant, String name, String group) {
        String format = redisService.get(getKey(tenant, name, FORMAT_KEY)) ?: '%d'
        String.format(format, nextNumberLong(tenant, name, group))
    }

    /**
     * Get next unique (raw) number.
     *
     * @param tenant tenant ID
     * @param name sequence name
     * @param group sub-sequence
     * @return number as a long
     */
    @Override
    T nextNumberLong(long tenant, String name, String group) {
        Long.valueOf(redisService.incr(getKey(tenant, name, group))) - 1L
    }

    /**
     * Update sequence.
     *
     * @param tenant tenant ID
     * @param name sequence name
     * @param group sub-sequence
     * @param format number format
     * @param current current number
     * @param start new number
     * @return sequence status if sequence was updated, null otherwise
     */
    @Override
    SequenceStatus update(long tenant, String name, String group, String format, T current, T start) {
        redisService.withRedis { redis ->
            if (format) {
                redis.set(getKey(tenant, name, FORMAT_KEY), format)
            } else {
                format = redis.get(getKey(tenant, name, FORMAT_KEY)) ?: '%d'
            }
            if (start != null) {
                redis.set(getKey(tenant, name, group), start.toString())
            } else {
                start = Long.valueOf(redis.get(getKey(tenant, name, group)))
            }
        }
        new SequenceStatus(name, format, start)
    }

    @Override
    SequenceStatus status(long tenant, String name, String group) {
        redisService.withRedis { redis ->
            String format = redis.get(getKey(tenant, name, FORMAT_KEY)) ?: '%d'
            Long start = Long.valueOf(redis.get(getKey(tenant, name, group)))
            new SequenceStatus(name, format, start)
        }
    }

    /**
     * Get sequence statistics.
     *
     * @param tenant tenant ID
     * @return statistics for all sequences in the tenant
     */
    @Override
    Iterable<SequenceStatus> getStatistics(long tenant) {
        return null
    }

    @Override
    void shutdown() {
        // Nothing to do here.
    }
}
