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

package moe.encode.airblock.commands.contrib.sessions;

import moe.encode.airblock.commands.Executor;
import moe.encode.airblock.commands.core.backend.Handle;
import moe.encode.airblock.commands.core.backend.HandleWrapper;
import moe.encode.airblock.commands.core.settings.Environment;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * <p>A test for the session manager.</p>
 *
 * <p>
 *     The session system is one of the most important parts of the session
 *     manager.
 * </p>
 */
public class SessionManagerTest {

    public static class FakeHandle extends Handle {

        UUID uuid;

        public FakeHandle(Object handle, UUID uuid) {
            super(handle);
            this.uuid = uuid;
        }

        @Nullable
        @Override
        public UUID getUniqueIdentifier() {
            return uuid;
        }

        @Override
        @SuppressWarnings("unchecked")
        public HandleWrapper<? extends Handle> wrap() {
            HandleWrapper wrapper = mock(HandleWrapper.class);
            when(wrapper.getHandle()).thenReturn(this);
            return wrapper;
        }
    }

    /**
     * Simple Session implementation.
     */
    public static class SessionImpl extends Session {

    }

    /**
     * Additional Session implementation.
     */
    public static class SessionImpl2 extends Session {

    }

    @Test
    public void testGetSession() throws Exception {
        SessionManager manager = new SessionManager(mock(Environment.class));

        UUID sameHandle = UUID.randomUUID();
        Handle a = this.createMockHandle(sameHandle);
        Handle b = this.createMockHandle(sameHandle);
        assertTrue("Asserting that the handles A and B are the same.", a.equals(b));

        Handle c = this.createMockHandle(UUID.randomUUID());
        assertFalse("Asserting that the handles A and C are not the same.", a.equals(c));
        assertFalse("Asserting that the handles B and C are not the same.", b.equals(c));

        SessionImpl impl = manager.getSession(a, SessionImpl.class);
        assertTrue("Session instance for B should be the same as for A.", impl == manager.getSession(b, SessionImpl.class));

        assertTrue("Session instance for handle C should be a new one.", impl != manager.getSession(c, SessionImpl.class));
    }

    @Test
    public void testGetSessions() throws Exception {
        Handle a = this.createMockHandle(UUID.randomUUID());
        Handle b = this.createMockHandle(UUID.randomUUID());
        assertFalse("Asserting that the handles A and B are not the same.", a.equals(b));

        SessionManager manager = new SessionManager(mock(Environment.class));
        manager.getSession(a, SessionImpl.class);
        manager.getSession(a, SessionImpl2.class);
        manager.getSession(b, SessionImpl.class);
        assertEquals(2, manager.getSessions(a).size());
        assertEquals(1, manager.getSessions(b).size());
    }

    @Test
    public void testSessionOwnership() throws Exception {
        UUID uuid = UUID.randomUUID();
        Handle a = this.createMockHandle(uuid);

        SessionManager manager = new SessionManager(mock(Environment.class));
        manager.getSession(a, SessionImpl.class);

        SessionImpl impl = manager.getSession(this.createMockHandle(uuid), SessionImpl.class);
        assertEquals(a, impl.getOwner().getHandle());
    }

    /**
     * Creates a mock hand
     * @param uuid Unique identifier that should be returned.
     * @return The unique identifier of the manager.
     */
    private Handle createMockHandle(UUID uuid) {
        return new FakeHandle(mock(Object.class), uuid);
    }
}