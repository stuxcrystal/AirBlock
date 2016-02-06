/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.encode.airblock.commands.core.backend;

import org.junit.Test;

import javax.annotation.Nullable;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class HandleTest {

    public static class FakeHandle1 extends Handle {

        private final UUID uuid;

        public FakeHandle1(Object handle, UUID uuid) {
            super(handle);
            this.uuid = uuid;
        }

        @Nullable
        @Override
        public UUID getUniqueIdentifier() {
            return this.uuid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public HandleWrapper<? extends Handle> wrap() {
            return mock(HandleWrapper.class);
        }

        public String toString() {
            return "FH1<" + this.getHandle() + ";" + this.getUniqueIdentifier() + ">";
        }
    }

    public static class FakeHandle2 extends Handle {

        private final UUID uuid;

        public FakeHandle2(Object handle, UUID uuid) {
            super(handle);
            this.uuid = uuid;
        }

        @Nullable
        @Override
        public UUID getUniqueIdentifier() {
            return this.uuid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public HandleWrapper<? extends Handle> wrap() {
            return mock(HandleWrapper.class);
        }

        public String toString() {
            return "FH2<" + this.getHandle() + ";" + this.getUniqueIdentifier() + ">";
        }
    }

    @Test
    public void testEquals() throws Exception {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        assertNotEquals("UUIDs should never be the same.", uuid1, uuid2);

        Object o1 = "O1";
        Object o2 = "O2";

        Handle h1 = new FakeHandle1(o1, uuid1);
        Handle h2 = new FakeHandle1(o1, uuid2);
        Handle h3 = new FakeHandle2(o1, uuid1);

        assertNotEquals(h1, h2);
        assertNotEquals(h2, h1);
        assertNotEquals(h1, h3);
        assertNotEquals(h2, h3);

        Handle h4 = new FakeHandle1(o1, uuid1);
        Handle h5 = new FakeHandle1(o2, uuid2);
        assertEquals(h1, h4);
        // assertEquals(h1, h5);

        Handle h6 = new FakeHandle1(o1, null);
        Handle h7 = new FakeHandle1(o1, null);
        assertNotEquals(h1, h6);
        assertEquals(h6, h7);

        Handle h8 = new FakeHandle1(o2, null);
        Handle h9 = new FakeHandle2(o1, null);
        assertNotEquals(h6, h8);
        assertNotEquals(h6, h9);
    }
}