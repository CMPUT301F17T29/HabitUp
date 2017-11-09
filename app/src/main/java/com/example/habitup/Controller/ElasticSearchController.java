package com.example.habitup.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

// http://cmput301.softwareprocess.es:8080/team29_habitup/

public class ElasticSearchController {
    private static JestDroidClient client;


    public static class AddUsersTask extends AsyncTask<UserAccount, Void, Void> {

        @Override
        protected Void doInBackground(UserAccount... users) {

            verifySettings();

            for (UserAccount user : users) {
                Index index = new Index.Builder(user).index("team29_habitup").type("user").build();


                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {

                        //set id of user
                    }

                    else{
                        Log.i("Error", "Elasticsearch was not able to add the User");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the User");
                }
            }

            return null;
        }
    }


    public static class AddHabitsTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits) {

            verifySettings();

            for (Habit habit : habits) {
                Index index = new Index.Builder(habit).index("team29_habitup").type("habit").build();


                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {

                        Log.i("Error", "Elasticsearch was not able to add the Habit");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the Habit");
                }
            }

            return null;
        }

    }

    public static class AddHabitEventsTask extends AsyncTask<HabitEvent, Void, Void> {

        @Override
        protected Void doInBackground(HabitEvent... habitEvents) {

            verifySettings();

            for (HabitEvent habitEvent : habitEvents) {
                Index index = new Index.Builder(habitEvent).index("team29_habitup").type("habitevent").build();

                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {

                        Log.i("Error", "Elasticsearch was not able to add the Habit Event");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the Habit Event");
                }
            }

            return null;
        }

    }

    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<UserAccount>>{

        @Override
        protected ArrayList<UserAccount> doInBackground(String... usernames) {
            verifySettings();

            ArrayList<UserAccount> users = new ArrayList<UserAccount>();

            //Build the query
            //String query = "{\n" +
                   // " \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder(usernames[0]).addIndex("team29_habitup").addType("user").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserAccount> foundUser = result.getSourceAsObjectList(UserAccount.class);
                    users.addAll(foundUser);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }


    //get Habit by name
    //returns an arraylist, should be length 1 with arrayList[0] being the ahbit
    public static class GetHabitsTask extends AsyncTask<String, Void, ArrayList<Habit>>{

        @Override
        protected ArrayList<Habit> doInBackground(String... habitnames) {
            verifySettings();

            ArrayList<Habit> habits = new ArrayList<Habit>();

            //Build the query
            //String query = "{\n" +
                    //" \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder(habitnames[0]).addIndex("team29_habitup").addType("habit").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Habit> foundHabit = result.getSourceAsObjectList(Habit.class);
                    habits.addAll(foundHabit);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habits;
        }
    }

    //need HabitEvent ID
    public static class GetHabitEventsTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... habitEventIds) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            //Build the query
            //String query = "{\n" +
                    //" \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder(habitEventIds[0]).addIndex("team29_habitup").addType("habitevent").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<HabitEvent> foundHabitEvent = result.getSourceAsObjectList(HabitEvent.class);
                    habitEvents.addAll(foundHabitEvent);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habitEvents;
        }
    }

    //gets all users in ES
    public static class GetAllUsersTask extends AsyncTask<Void, Void, ArrayList<UserAccount>>{

        @Override
        protected ArrayList<UserAccount> doInBackground(Void... voids) {
            verifySettings();

            ArrayList<UserAccount> users = new ArrayList<UserAccount>();

            //Build the query
            //String query = "{\n" +
            // " \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder("").addIndex("team29_habitup").addType("user").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserAccount> foundUser = result.getSourceAsObjectList(UserAccount.class);
                    users.addAll(foundUser);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }


    //get all Habits in ES
    public static class GetAllHabitsTask extends AsyncTask<Void, Void, ArrayList<Habit>>{

        @Override
        protected ArrayList<Habit> doInBackground(Void ...voids) {
            verifySettings();

            ArrayList<Habit> habits = new ArrayList<Habit>();

            //Build the query
            //String query = "{\n" +
            //" \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder("").addIndex("team29_habitup").addType("habit").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Habit> foundHabit = result.getSourceAsObjectList(Habit.class);
                    habits.addAll(foundHabit);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habits;
        }
    }

    //Gets all habit events in ES
    public static class GetAllHabitEventsTask extends AsyncTask<Void, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(Void... voids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            //Build the query
            //String query = "{\n" +
            //" \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder("").addIndex("team29_habitup").addType("habitevent").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<HabitEvent> foundHabitEvent = result.getSourceAsObjectList(HabitEvent.class);
                    habitEvents.addAll(foundHabitEvent);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habitEvents;
        }
    }


    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
