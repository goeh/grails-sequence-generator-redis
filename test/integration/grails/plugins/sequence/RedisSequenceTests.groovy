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

/**
 * Tests for the Sequence Generator backed by Redis.
 */
class RedisSequenceTests extends GroovyTestCase {

    def sequenceGenerator

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        sequenceGenerator.delete(0L, "test", null)
    }

    void testCreateSequence() {
        def s = sequenceGenerator.create(0L, "test", null, "%04d", 42L)
        assertEquals "test", s.name
        assertEquals "%04d", s.format
        assertEquals "0042", s.getNumberFormatted()

        s = sequenceGenerator.status(0L, "test", null)
        assertEquals "test", s.name
        assertEquals "%04d", s.format
        assertEquals "0042", s.getNumberFormatted()

        assertEquals "0042", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals "0043", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals "0044", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals "0045", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals "0046", sequenceGenerator.nextNumber(0L, "test", null)

        s = sequenceGenerator.update(0L, "test", null, "%03d", 47L, 99L)
        assertEquals "test", s.name
        assertEquals "%03d", s.format
        assertEquals "099", s.getNumberFormatted()

        assertEquals "099", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals "100", sequenceGenerator.nextNumber(0L, "test", null)
        assertEquals 101L, sequenceGenerator.nextNumberLong(0L, "test", null)
    }
}
