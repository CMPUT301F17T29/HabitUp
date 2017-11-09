package com.example.habitup.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.UserAccount;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

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
                    if (!result.isSucceeded()) {

                        Log.i("Error", "Elasticsearch was not able to add the tweet");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the tweets");
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

                        Log.i("Error", "Elasticsearch was not able to add the tweet");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the tweets");
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

                        Log.i("Error", "Elasticsearch was not able to add the tweet");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the tweets");
                }
            }

            return null;
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
