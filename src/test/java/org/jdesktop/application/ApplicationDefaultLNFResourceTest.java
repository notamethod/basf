/*
* Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
* subject to license terms.
*/

package org.jdesktop.application;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

/**
 * The Application.lookAndFeel resource defined as "default", which
 * indicates that we should load the JVM default look and feel.
 * That's metal on all platforms except OSX, where it's the native L&F.
 *
 * This test depends on resources/AppilcationDefaultLNF.properties
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ApplicationDefaultLNFResourceTest
{

    public static class ApplicationDefaultLNF extends WaitForStartupApplication
    {
    }

    @Before
    public void methodSetup()
    {
        ApplicationDefaultLNF.launchAndWait(ApplicationDefaultLNF.class);
    }

    @Test
    public void testApplicationLookAndFeelResource()
    {
        LookAndFeel lnf = UIManager.getLookAndFeel();
        String osName = System.getProperty("os.name");
        if ((osName != null) && (osName.toLowerCase().startsWith("mac os x")))
        {
            assertTrue("OSX default L&F is native", lnf.isNativeLookAndFeel());
        }
        if ((osName != null) && (!osName.toLowerCase().startsWith("linux")))
        {
            // Difference between Gnome (and forks) and Non-Gnome desktops:
            // GTKLookAndFeel is default LF for Gnome
            // checking $XDG_CURRENT_DESKTOP doesn't work with Gnome forks.
            assertFalse("not native L&F "+lnf.getClass().getName(), lnf.isNativeLookAndFeel());
            String defaultLNFName = UIManager.getCrossPlatformLookAndFeelClassName();
            System.out.println(defaultLNFName);

            assertEquals("cross platform L&F name", defaultLNFName, lnf.getClass().getName());
        }
        else
        {

        }
    }
}
