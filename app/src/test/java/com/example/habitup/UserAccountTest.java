package com.example.habitup;

import android.graphics.Bitmap;

import com.example.habitup.Model.UserAccount;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Ty on 2017-11-12.
 */

public class UserAccountTest {

    // Test to ensure set username is equal to the String given
    @Test
    public void testSetUsernameSuccessful() {


        String u1 = "gojeffcho";
        String u2 = "Jeff Cho";
        UserAccount testAccount = new UserAccount(u1, u2, null);

        assertTrue(testAccount.getUsername() == u1);
    }

    // Test to ensure username is not set if it is too long
    @Test
    public void testSetUsernameLengthLimit() {

        String u1 = "gojeffchowritesareallylongusernamethatisillegal";
        String u2 = "Jeff Cho";

        // This should throw an exception
        try {
            UserAccount testAccount = new UserAccount(u1, u2, null);
        } catch (IllegalArgumentException e) {
            assertTrue(Boolean.TRUE);
            return;
        }

        // If it didn't, we have a problem
        assertTrue(Boolean.FALSE);

    }

    // Test to ensure set realname is equal to the String given
    @Test
    public void testSetRealname() {

        String u1 = "gojeffcho";
        String u2 = "Jeff Cho";
        UserAccount testAccount = new UserAccount(u1, u2, null);

        assertTrue(testAccount.getRealname() == u2);
    }

    // Test to ensure set realname is within character limits
    @Test
    public void testSetRealnameLengthLimit() {

        String u1 = "gojeffcho";
        String u2 = "Jeff Cho is writing an extremely long string as realname";


        // This should throw an exception
        try {
            UserAccount testAccount = new UserAccount(u1, u2, null);
        } catch (IllegalArgumentException e) {
            assertTrue(Boolean.TRUE);
            return;
        }

        // If it didn't, we have a problem
        assertTrue(Boolean.FALSE);

    }

    // TODO: test photo?

    //Tests setting and Getting the photo
    @Test
    public void testSetPhoto() {
        Bitmap bm = Bitmap.createBitmap(666666, 666666, Bitmap.Config.ARGB_8888);
        String u1 = "gojeffcho";
        String u2 = "Jeff Cho";
        UserAccount testAccount = new UserAccount(u1, u2, null);
        testAccount.setPhoto(bm);

        assertTrue(testAccount.getPhoto() == bm);
    }

    @Test
    public void testGetPhoto() {
        Bitmap bm = Bitmap.createBitmap(666666, 666666, Bitmap.Config.ARGB_8888);
        String u1 = "gojeffcho";
        String u2 = "Jeff Cho";
        UserAccount testAccount = new UserAccount(u1, u2, null);
        testAccount.setPhoto(bm);

    }


    // Test incrementLevel()
    @Test
    public void testIncrementLevel() {

        // Level == 1 on init
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);

        // Level == 2 on increment
        testAccount.incrementLevel();
        assertTrue(testAccount.getLevel() == 2);

    }

    // Test incrementLevel() at int boundary
    @Test
    public void testIncrementLevelMax() {

        // Level == 1 on init
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);

        // Get level to MAX_INT
        for (int i = 1; i < Integer.MAX_VALUE; ++i) {
            testAccount.incrementLevel();
        }

        // Confirm MAX_INT
        assertTrue(testAccount.getLevel() == Integer.MAX_VALUE);

        // Attempt overflow - must not add
        testAccount.incrementLevel();
        assertTrue(testAccount.getLevel() == Integer.MAX_VALUE);
    }

    // Test increaseXP
    @Test
    public void testIncreaseXP() {

        // XP == 0 on init
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);

        // XP == 10
        testAccount.increaseXP(10);
        assertTrue(testAccount.getXP() == 10);

        // XP == 20
        testAccount.increaseXP(15);
        assertTrue(testAccount.getXP() == 25);

    }

    // Test increaseXP upper bound
    @Test
    public void testIncreaseXPMax() {

        // XP == 0 on init
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);

        // XP == INT_MAX
        testAccount.increaseXP(Integer.MAX_VALUE);
        assertTrue(testAccount.getXP() == Integer.MAX_VALUE);

        // XP++ - must not increase
        testAccount.increaseXP(1);
        assertTrue(testAccount.getXP() == Integer.MAX_VALUE);
    }
/*
    // Test setXPtoNext()
    @Test
    public void testSetXPtoNext() {

        // Get first XP bar to next level
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        int next = testAccount.getXPtoNext();

        // Increase XP by that much
        testAccount.increaseXP(next);
        System.out.println("next: " + next + " getXPtoNext: " + testAccount.getXPtoNext());

        // Make sure a new target was set
        assertTrue(testAccount.getXPtoNext() > next);
    }
*/

}
