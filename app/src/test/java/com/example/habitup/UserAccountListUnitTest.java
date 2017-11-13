package com.example.habitup;


import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UserAccountListUnitTest {

    @Test
    public void testUserAccountListContains(){
        UserAccountList testUserAccountList = new UserAccountList();

        String u1 = "tyiscool";
        String u2 = "Tyler Heise";

        UserAccount user1 = new UserAccount(u1, u2, null);

        assertFalse(testUserAccountList.contains(user1));

        testUserAccountList.add(user1);

        assertTrue(testUserAccountList.contains(user1));
    }

    @Test
    public void testUserAccountListAdd(){

        String u1 = "tyiscool";
        String u2 = "Tyler Heise";

        UserAccountList testUserAccountList = new UserAccountList();

        UserAccount user1 = new UserAccount(u1, u2, null);

        testUserAccountList.add(user1);
        assertTrue(testUserAccountList.contains(user1));

    }

    //Test add does not add duplicates
    @Test
    public void testUserAccountListAddDuplicate(){
        String u1 = "tyiscool";
        String u2 = "Tyler Heise";

        UserAccountList testUserAccountList = new UserAccountList();
        UserAccount user1 = new UserAccount(u1, u2, null);

        testUserAccountList.add(user1);
        testUserAccountList.add(user1);

        assertEquals(-1,testUserAccountList.add(user1));
    }

    @Test
    public void testUserAccountsListSize(){
        UserAccountList list = new UserAccountList();

        String u1 = "tyiscool";
        String u2 = "Tyler Heise";

        UserAccount user1 = new UserAccount(u1, u2, null);
        list.add(user1);
        assertTrue(list.size() == 1);

        UserAccount user2 = new UserAccount("billgates", "Bill Gates", null);
        list.add(user2);
        assertTrue(list.size() == 2);

    }


}
