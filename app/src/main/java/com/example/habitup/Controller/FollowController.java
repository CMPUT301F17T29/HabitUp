package com.example.habitup.Controller;

import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;

/**
 * Created by barboza on 2017-11-26.
 */

public class FollowController {
    /**
     * Remove a follow request from the user's requests list.
     * @param followee the UserAccount that the request is deleted from
     * @param follower the UserAccount that sent the request
     * @return 1 if user successfully removed
     */
    static public int removeFriendRequest(UserAccount followee, UserAccount follower) throws IllegalArgumentException {
        UserAccountList requestList = followee.getRequestList();

        if (requestList.delete(follower) == 0) {
            HabitUpApplication.updateUser(followee);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: Failed to remove friend request.");
        }
    }


    /**
     * Add another user to the user's requests list.
     * @param followee the UserAccount being sent the request
     * @param follower the UserAccount that is sending the request
     * @return 1 if request successfully added
     */
    static public int addFriendRequest(UserAccount followee, UserAccount follower) throws IllegalArgumentException {
        UserAccountList requestList = followee.getRequestList();

        if (requestList.add(follower) == 0) {
            HabitUpApplication.updateUser(followee);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: Failed to add friend request.");
        }
    }

    /**
     * Add another user to the user's friends list.
     * @param followee the UserAccount that granted the friend request
     * @param follower the UserAccount that requested to be a friend
     * @return 1 if user successfully added
     */
    static public int addFriend(UserAccount followee, UserAccount follower) throws IllegalArgumentException {
        UserAccountList friendList = follower.getFriendsList();

        if (friendList.add(followee) == 0) {
            HabitUpApplication.updateUser(follower);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: Failed to add friend.");
        }
    }

    /**
     * Remove a user from a user's friends list.
     * @param user the user deleting a friend
     * @param friend the friend to be removed
     * @return 1 if friend successfully removed
     * @throws IllegalArgumentException
     */
    static public int removeFriend(UserAccount user, UserAccount friend) throws IllegalArgumentException {
        UserAccountList friendList = user.getFriendsList();

        if (friendList.delete(friend) == 0) {
            HabitUpApplication.updateUser(user);
            return 0;
        } else {
            throw new IllegalArgumentException("Error: Failed to remove friend.");
        }
    }
}
