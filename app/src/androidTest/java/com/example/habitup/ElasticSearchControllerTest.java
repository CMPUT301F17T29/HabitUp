package com.example.habitup;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;


/**
 * Created by Ty on 2017-11-08.
 */
@RunWith(AndroidJUnit4.class)
public class ElasticSearchControllerTest {

    @Test
    public void testAddUsersTask() {
        UserAccount u1 = new UserAccount("theise", "Tyler Heise", null);
        UserAccount u2 = new UserAccount("ezakirova", "E Z", null);

        //private ArrayList<UserAccount> userList = new ArrayList<UserAccount>();
        String x = u1.getUsername();
        String y = u2.getUsername();

        ElasticSearchController.AddUsersTask addUsersTask = new ElasticSearchController.AddUsersTask();

        addUsersTask.execute(u2);
    }

    @Test
    public void testGetUsersTask() {

        ElasticSearchController.GetUsersTask getUserTask = new ElasticSearchController.GetUsersTask();

        ArrayList<UserAccount> users = new ArrayList<UserAccount>();

        getUserTask.execute("theise");

        try {
            users = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User from the async object");
        }

        assert (users.get(0).getUsername().equals("theise"));

        assert (users.size() == 1);

        ElasticSearchController.GetUsersTask getUserTask1 = new ElasticSearchController.GetUsersTask();

        getUserTask1.execute("ezakirova");

        try {
            users = getUserTask1.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User from the async object");
        }

        assert (users.get(0).getUsername().equals("ezakirova"));

        assert (users.size() == 1);
    }

    /*@Test
    public void testAddHabitTask() {
        Habit h1 = new Habit(1, "testHabit1", "to test", null, null, null);
        Habit h2 = new Habit(2, "testHabit2", "to test", null, null, null);

        ElasticSearchController.AddHabitsTask addHabitsTask = new ElasticSearchController.AddHabitsTask();

        addHabitsTask.execute(h1);

        ElasticSearchController.AddHabitsTask addHabitsTask2 = new ElasticSearchController.AddHabitsTask();

        addHabitsTask2.execute(h2);

    }

    @Test
    public void testGetAllHabitsTask() {
        ElasticSearchController.GetAllHabitsTask getAllHabitsTask = new ElasticSearchController.GetAllHabitsTask();

        getAllHabitsTask.execute();

        ArrayList<Habit> habits = new ArrayList<Habit>();

        try {
            habits = getAllHabitsTask.get();

        } catch (Exception e) {
            Log.i("Error", "Failed to get the Habits from the async object");
        }

        assert (habits.size() == 2);
    }
*/

    @Test
    public void testAddHabitEventTask() {
        HabitEvent he1 = new HabitEvent(1, null, "vnice", null);
        HabitEvent he2 = new HabitEvent(2, null, "Not nice" , null);

        ElasticSearchController.AddHabitEventsTask addHabitEventsTask = new ElasticSearchController.AddHabitEventsTask();

        addHabitEventsTask.execute(he1);

        ElasticSearchController.AddHabitEventsTask addHabitEventsTask2 = new ElasticSearchController.AddHabitEventsTask();

        addHabitEventsTask2.execute(he2);

    }


    @Test
    public void testGetAllHabitEventsTask() {
        ElasticSearchController.GetAllHabitEventsTask getAllHabitEventsTask = new ElasticSearchController.GetAllHabitEventsTask();

        getAllHabitEventsTask.execute();

        ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

        try {
            habitEvents = getAllHabitEventsTask.get();

        } catch (Exception e) {
            Log.i("Error", "Failed to get the Habits from the async object");
        }

        assert (habitEvents.size() == 2);
        assert (habitEvents.get(0).getComment().equals("vnice"));
        assert (habitEvents.get(1).getComment().equals("Not nice"));
    }
}
