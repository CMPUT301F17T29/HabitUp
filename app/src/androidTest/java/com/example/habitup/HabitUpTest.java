package com.example.habitup;

import android.media.Image;
import android.support.test.runner.AndroidJUnit4;

import com.example.habitup.Model.HabitUp;
import com.example.habitup.Model.UserAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

import java.util.ArrayList;


/**
 * Created by Ty on 2017-10-21.
 *
 * Tests implementation of HabitUp
 *
 */
@RunWith(AndroidJUnit4.class)
public class HabitUpTest {

    //Tests that addNewUser puts the new user into the array list
    @Test
    public void testAddNewUserSuccesful(){
        String u1 = "tyiscool";
        String u2 = "Tyler Heise";
        Image photo = null;

        HabitUp testHabitUp = new HabitUp();

        UserAccount user1 = UserAccount(u1, u2, photo);

        testHabitUp.addNewUser(user1);
        assertTrue(testHabitUp.getUsers().contains(user1));

    }

    // Tests that the login method appropriately sets current user
    @Test
    public void testLogin(){
        String u1 = "tyiscool";
        String u2 = "Tyler Heise";
        Image photo = null;

        HabitUp testHabitUp = new HabitUp();

        UserAccount user1 = UserAccount(u1, u2, photo);

        testHabitUp.addNewUser(user1);

        testHabitUp.login(u1);
        assertTrue(testHabitUp.getCurrentUser() == user1);

        //this should throw an exception
        try {
            String invalidName = "thisIsNotARealUser";
            testHabitUp.login(invalidName);

        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

        //If it didn't, we have a problem
        assertTrue(Boolean.FALSE);
    }

    //tests that the local load file method loads file properly
    @Test
    public void testLoad(){
        //test that file loaded is correct
        HabitUp testHabitUp = new HabitUp();
        testHabitUp.loadFile();

        //generate list the same as one contained in file
        String u1 = "tyiscool";
        String u2 = "Tyler Heise";
        Image photo = null;

        UserAccount user1 = UserAccount(u1, u2, photo);

        ArrayList<UserAccount> testList = new ArrayList<UserAccount>();
        testList.add(user1);

        //generated list and loaded list should be the same
        assertTrue(testHabitUp.getUsers() == testList);

        //generate new user to make sure lists aren't always equal
        String u3 = "nightswatch1992";
        String u4 = "John Snow";
        Image photo2 = null;

        UserAccount user2 = UserAccount(u3, u4, photo2);
        testList.add(user2);

        assertTrue(testHabitUp.getUsers() != testList);
    }

    //tests that the local save file method saves the file properly
    @Test
    public void testSave(){
        //add a user to HabitUp users array list
        HabitUp testHabitUp = new HabitUp();

        String u1 = "tyiscool";
        String u2 = "Tyler Heise";
        Image photo = null;

        UserAccount user1 = UserAccount(u1, u2, photo);
        testHabitUp.addNewUser(user1);

        //save and load array list
        testHabitUp.saveFile();
        testHabitUp.loadFile();

        //generate test list the same as the one that was saved
        ArrayList<UserAccount> testList = new ArrayList<UserAccount>();
        testList.add(user1);

        //if save worked properly lists should be the same
        assertTrue(testHabitUp.getUsers() == testList);

        //generate new user to make sure lists aren't always equal
        String u3 = "nightswatch1992";
        String u4 = "John Snow";
        Image photo2 = null;

        UserAccount user2 = UserAccount(u3, u4, photo2);
        testHabitUp.addNewUser(user2);

        testHabitUp.saveFile();
        testHabitUp.loadFile();

        assertTrue(testHabitUp.getUsers() != testList);

    }

}
