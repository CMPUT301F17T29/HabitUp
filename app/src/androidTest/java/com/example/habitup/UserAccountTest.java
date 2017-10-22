package com.example.habitup;

import android.media.Image;
import android.support.test.runner.AndroidJUnit4;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

/**
 * Created by gojeffcho on 2017-10-20.
 *
 * Tests implementation of UserAccount and runs it through its various paces.
 */
@RunWith(AndroidJUnit4.class)
public class UserAccountTest {


    // Test to ensure set username is equal to the String given
    @Test
    public void testSetUsernameSuccessful() {

        String u1 = "gojeffcho";
        String u2 = "Jeff Cho";
        Image photo = null;
        UserAccount testAccount = new UserAccount(u1, u2, photo);

        assertTrue(testAccount.getUsername() == u1);
    }

    // Test to ensure username is not set if it is too long
    @Test
    public void testSetUsernameLengthLimit() {

        String u1 = "gojeffchowritesareallylongusernamethatisillegal";
        String u2 = "Jeff Cho";
        Image photo = null;

        // This should throw an exception
        try {
            UserAccount testAccount = new UserAccount(u1, u2, photo);
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

    // Test setXPtoNext()
    @Test
    public void testSetXPtoNext() {

        // Get first XP bar to next level
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        int next = testAccount.getXPtoNext();

        // Increase XP by that much
        testAccount.increaseXP(next);

        // Make sure a new target was set
        assertTrue(testAccount.getXPtoNext() > next);
    }

    // Test addRequest()
    @Test
    public void testAddRequest() {

        // Set up UserAccount, requestingUser
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        UserAccount testAccount2 = new UserAccount("gojeffcho2", "Joff Cha", null);

        // Requests empty
        assertTrue(testAccount.getRequestsPending().isEmpty());

        // Add Request
        testAccount.addRequest(testAccount2);

        // Verify Request in List
        assertTrue(testAccount.getRequestsPending().contains(testAccount2));
    }

    // Test addRequest() does not add duplicates
    @Test
    public void testAddRequestNoDuplicates() {

        // Set up UserAccount, requestingUser
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        UserAccount testAccount2 = new UserAccount("gojeffcho2", "Joff Cha", null);

        // Add Request, twice - it should only action once
        testAccount.addRequest(testAccount2);
        testAccount.addRequest(testAccount2);

        // Verify that it only actioned once by removing once then checking for user
        testAccount.getRequestsPending().remove(testAccount2);
        assertTrue(!testAccount.getRequestsPending().contains(testAccount2));
    }

    // Test approveRequest()
    @Test
    public void testApproveRequest() {

        // Set up UserAccount, requestingUser
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        UserAccount testAccount2 = new UserAccount("gojeffcho2", "Jeef Coo", null);

        // Add the request, then approve it
        testAccount.addRequest(testAccount2);
        testAccount.approveRequest(testAccount2);

        // Request no longer in requestsPending, user in friendsList
        assertTrue(!testAccount.getRequestsPending().contains(testAccount2));
        assertTrue(testAccount.getFriendsList().contains(testAccount2));
    }

    // Test approveRequest() only approves if user is in requestsPending
    @Test
    public void testApproveRequestOnlyIfInPending() {

        // Set up UserAccount, requestingUser
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        UserAccount testAccount2 = new UserAccount("gojeffcho2", "Juff Pho", null);
        UserAccount testAccount3 = new UserAccount("gojeffcho3", "Jfff Chh", null);

        // Add the request, then approve one that's not in requestsPending
        testAccount.addRequest(testAccount2);
        testAccount.approveRequest(testAccount3);

        // Check that account2 is still in pending, account3 is not in friends
        assertTrue(testAccount.getRequestsPending().contains(testAccount2));
        assertTrue(!testAccount.getFriendsList().contains(testAccount3));
    }

    // Test addHabit
    @Test
    public void testAddHabit() {

        // Set up UserAccount, Habit
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        Habit testHabit = new Habit();

        // Check Habits empty; add the Habit
        assertTrue(testAccount.getHabits().isEmpty());
        testAccount.addHabit(testHabit);

        // Make sure Habit got added
        assertTrue(testAccount.getHabits().contains(testHabit));
    }

    // Test deleteHabit
    @Test
    public void testDeleteHabit() {

        // Set up UserAccount, Habit
        UserAccount testAccount = new UserAccount("gojeffcho", "Jeff Cho", null);
        Habit testHabit = new Habit();

        // Add the Habit, then delete the habit
        testAccount.addHabit(testHabit);
        testAccount.deleteHabit(testHabit);

        // Make sure Habit got deleted
        assertTrue(!testAccount.getHabits().contains(testHabit));
    }

}
