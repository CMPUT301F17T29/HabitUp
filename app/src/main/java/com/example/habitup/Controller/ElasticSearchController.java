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

public class ElasticSearchController {
    private static JestDroidClient client;

    //http://cmput301.softwareprocess.es:8080/Team29_HabitUp/

    public static class AddUsersTask extends AsyncTask<UserAccount, Void, Void> {

        @Override
        protected Void doInBackground(UserAccount... users) {

            verifySettings();

            for (UserAccount user : users) {
                Index index = new Index.Builder(user).index("Team29_HabitUp").type("user").build();


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
                Index index = new Index.Builder(habit).index("Team29_HabitUp").type("habit").build();


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
                Index index = new Index.Builder(habitEvent).index("Team29_HabitUp").type("habitEvent").build();

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

    public static class GetUserTask extends AsyncTask<String, Void, ArrayList<UserAccount>>{

        @Override
        protected ArrayList<UserAccount> doInBackground(String... usernames) {
            verifySettings();

            ArrayList<UserAccount> users = new ArrayList<UserAccount>();

            //Build the query
            String query = "{\n" +
                    " \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            Search search = new Search.Builder(usernames[0]).addIndex("Team29_HabitUp").addType("user").build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserAccount> foundUser = result.getSourceAsObjectList(UserAccount.class);
                    users.addAll(foundUser);

                    // DEBUG: print the tweets we got
                    for (UserAccount user : users) {
                        Log.i("user:", user.getUsername());
                    }

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
