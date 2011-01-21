/*
* Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
* subject to license terms.
*/


package org.jdesktop.application;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * Test the internal (package private) MnemonicText class.
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */

public class MnemonicTextTest
{

    /* This javax.swing.Action constants is only 
     * defined in Mustang (1.6), see 
     * http://download.java.net/jdk6/docs/api/javax/swing/Action.html
     */
    private static final String DISPLAYED_MNEMONIC_INDEX_KEY = "SwingDisplayedMnemonicIndexKey";

    private void checkAction(javax.swing.Action target, String text, int mnemonicKey, int mnemonicIndex)
    {
        assertEquals(text, target.getValue(javax.swing.Action.NAME));
        if (mnemonicKey != KeyEvent.VK_UNDEFINED)
        {
            /* If no mnemonicKey marker is specified then these properties 
             * aren't set at all.
             */
            assertEquals(mnemonicKey, target.getValue(javax.swing.Action.MNEMONIC_KEY));
            assertEquals(mnemonicIndex, target.getValue(DISPLAYED_MNEMONIC_INDEX_KEY));
        }
    }

    private void checkButton(AbstractButton target, String text, int mnemonicKey, int mnemonicIndex)
    {
        assertEquals(text, target.getText());
        assertEquals(mnemonicKey, target.getMnemonic());
        assertEquals(mnemonicIndex, target.getDisplayedMnemonicIndex());
    }

    private void checkLabel(JLabel target, String text, int mnemonicKey, int mnemonicIndex)
    {
        assertEquals(text, target.getText());
        assertEquals(mnemonicKey, target.getDisplayedMnemonic());
        assertEquals(mnemonicIndex, target.getDisplayedMnemonicIndex());
    }

    private static class MnemonicData
    {
        String markedText;
        String text;
        int mnemonicKey = KeyEvent.VK_UNDEFINED;
        int mnemonicIndex = -1;

        MnemonicData(String text)
        {
            this.markedText = this.text = text;
        }

        MnemonicData(String markedText, String text, int mnemonicKey, int mnemonicIndex)
        {
            this.markedText = markedText;
            this.text = text;
            this.mnemonicKey = mnemonicKey;
            this.mnemonicIndex = mnemonicIndex;
        }
    }

    @Test
    public void testConfigure()
    {
        MnemonicData[] testData = {
                new MnemonicData(""),  // text doesn't contain a valid mnemonic marker
                new MnemonicData("&"), // ...
                new MnemonicData("x"),
                new MnemonicData("xy"),
                new MnemonicData("xyz"),
                new MnemonicData("x&"),
                new MnemonicData("x& "),
                new MnemonicData("x&\t"),
                new MnemonicData("x & y"),
                new MnemonicData("'&'"),
                new MnemonicData("foo('&')"),
                new MnemonicData("&x & y", "x & y", KeyEvent.VK_X, 0), // *does* contain ...
                new MnemonicData("x & &y", "x & y", KeyEvent.VK_Y, 4), // ...
                new MnemonicData("&File", "File", KeyEvent.VK_F, 0),
                new MnemonicData("Save &As", "Save As", KeyEvent.VK_A, 5),
        };
        JLabel l = new JLabel();
        for (MnemonicData d : testData)
        {
            MnemonicText.configure(l, d.markedText);
            checkLabel(l, d.text, d.mnemonicKey, d.mnemonicIndex);
        }
        JButton b = new JButton();
        for (MnemonicData d : testData)
        {
            MnemonicText.configure(b, d.markedText);
            checkButton(b, d.text, d.mnemonicKey, d.mnemonicIndex);
        }
        javax.swing.Action a = new AbstractAction("test")
        {
            public void actionPerformed(ActionEvent e) { }
        };
        for (MnemonicData d : testData)
        {
            MnemonicText.configure(a, d.markedText);
            checkAction(a, d.text, d.mnemonicKey, d.mnemonicIndex);
        }
    }
}
