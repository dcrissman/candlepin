/**
 * Copyright (c) 2009 - 2012 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin;

import static org.mockito.Mockito.*;

import com.google.inject.AbstractModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CandlepinNonServletEnvironmentTestingModule extends AbstractModule {

    @Override
    public void configure() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("username")).thenReturn("mock_user");

        bind(HttpServletRequest.class).toInstance(request);
        bind(HttpServletResponse.class).toInstance(mock(HttpServletResponse.class));
    }
}
