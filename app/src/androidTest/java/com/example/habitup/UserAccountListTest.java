//package com.example.habitup;
//
//import android.media.Image;
//import android.support.test.runner.AndroidJUnit4;
//
//import com.example.habitup.Model.UserAccount;
//import com.example.habitup.Model.UserAccountList;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;
//
///**
// * Created by Ty on 2017-10-22.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class UserAccountListTest {
//
//    //Tests that add puts the new user into the array list
//    @Test
//    public void testUserAccountListAdd(){
//        String u1 = "tyiscool";
//        String u2 = "Tyler Heise";
//        Image photo = null;
//
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        UserAccount user1 = new UserAccount(u1, u2, photo);
//
//        testUserAccountList.add(user1);
//        assertTrue(testUserAccountList.contains(user1));
//
//    }
//
//    //Test add does not add duplicates
//    @Test
//    public void testUserAccountListAddDuplicate(){
//        String u1 = "tyiscool";
//        String u2 = "Tyler Heise";
//        Image photo = null;
//
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        UserAccount user1 = new UserAccount(u1, u2, photo);
//
//        testUserAccountList.add(user1);
//        testUserAccountList.add(user1);
//
//        testUserAccountList.remove(user1);
//
//        assertTrue(!testUserAccountList.contains(user1));
//    }
//
//    @Test
//    public void testUserAccountListContains(){
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        String u1 = "tyiscool";
//        String u2 = "Tyler Heise";
//        Image photo = null;
//
//        UserAccount user1 = new UserAccount(u1, u2, photo);
//
//        assertFalse(testUserAccountList.contains(user1));
//
//        testUserAccountList.add(user1);
//
//        assertTrue(testUserAccountList.contains(user1));
//    }
//
//    @Test
//    public void testUserAccountsListSize(){
//        UserAccountList list = new UserAccountList();
//
//        String u1 = "tyiscool";
//        String u2 = "Tyler Heise";
//        Image photo = null;
//
//        UserAccount user1 = new UserAccount(u1, u2, photo);
//        list.add(user1);
//        assertTrue(list.size() == 1);
//
//        UserAccount user2 = new UserAccount("billgates", "Bill Gates", photo);
//        list.add(user2);
//        assertTrue(list.size() == 2);
//
//    }
//
//    //tests that searchUsers() returns the user matching the search term
//    @Test
//    public void testUserAccountSearch(){
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        UserAccount user1 = new UserAccount("tyiscool", "Tyler Heise", null);
//        UserAccount user2 = new UserAccount("programmingiscool", "Bill Gates", null);
//
//        testUserAccountList.add(user1);
//
//        UserAccountList testSearchList;
//        testSearchList = testUserAccountList.searchUsers("tyiscool");
//
//        assertTrue(testSearchList.contains(user1));
//        assertFalse(testSearchList.contains(user2));
//
//    }
//
//    @Test
//    public void testUserAccountDelete(){
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        UserAccount user1 = new UserAccount("tyiscool", "Tyler Heise", null);
//        UserAccount user2 = new UserAccount("programmingiscool", "Bill Gates", null);
//
//        testUserAccountList.add(user1);
//        testUserAccountList.add(user2);
//
//        testUserAccountList.remove(user2);
//
//        assertFalse(testUserAccountList.contains(user2));
//    }
//
//    @Test
//    public void testUserAccountGetUser(){
//        UserAccountList testUserAccountList = new UserAccountList();
//
//        String u1 = "tyiscool", u2 = "programmingiscool";
//        UserAccount user1 = new UserAccount(u1, "Tyler Heise", null);
//        UserAccount user2 = new UserAccount(u2, "Bill Gates", null);
//
//        testUserAccountList.add(user1);
//        testUserAccountList.add(user2);
//
//        assertTrue(testUserAccountList.getUser(u1) == user1);
//        assertTrue(testUserAccountList.getUser(u1) != testUserAccountList.getUser(u2));
//    }
//
//}