package com.example.habitup.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.habitup.Model.Attributes;
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

    private static final String serverURL = "http://cmput301.softwareprocess.es:8080/";
    private static final String db = "team29_habitup";
    private static final String userType = "user";
    private static final String attrType = "attributes";
    private static final String habitType = "habit";
    private static final String habitEventType = "habitevent";


    public static class AddUsersTask extends AsyncTask<UserAccount, Void, Void> {

        @Override
        protected Void doInBackground(UserAccount... users) {
            verifySettings();

            for (UserAccount user : users) {
                Index index = new Index.Builder(user).index("team29_habitup").type("user").id(Integer.toString(user.getUID())).build();

                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {

                    }

                    else{
                        Log.i("Error", "Elasticsearch was not able to add the User");
                    }
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the User");

                }
            }

            return null;
        }
    }

    /* Given a username, returns the user object corresponding to it */
    public static class GetUser extends AsyncTask<String, Void, ArrayList<UserAccount>> {
        @Override
        protected ArrayList<UserAccount> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<UserAccount> accounts = new ArrayList<>();
            String UserQuery;

            if (search_parameters[0].equals("")){
                UserQuery = search_parameters[0];
            }

            else{
                UserQuery = "{\"query\": {\"match\" : { \"username\" : \"" + search_parameters[0] + "\" }}}";
            }

            //Log.i("Debug", "username to search for is: "+ search_parameters[0]);

            Search search = new Search.Builder(UserQuery)
                    .addIndex(db)
                    .addType(userType)
                    .build();

            try {
                // Send request to the server to get the user
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){

                    //Log.i("DeBug", "Succeeded in finding a user");

                    List<UserAccount> foundUsers = result.getSourceAsObjectList(UserAccount.class);
                    accounts.addAll(foundUsers);
                }
                else{
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return accounts;
        }
    }


    public static class AddAttrsTask extends AsyncTask<Attributes, Void, Void> {

        @Override
        protected Void doInBackground(Attributes... attrs) {

            verifySettings();

            for (Attributes attr : attrs) {
                Index index = new Index.Builder(attr).index(db).type(attrType).build();


                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {

                        //set id of user
                    }

                    else{
                        Log.i("Error", "Elasticsearch was not able to add the Attributes");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the Attributes");
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
                Index index = new Index.Builder(habit).index(db).type(habitType).id(Integer.toString(habit.getHID())).build();


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

    //get Habit by name
    //returns an arraylist, should be length 1 with arrayList[0] being the ahbit
    public static class GetHabitsTask extends AsyncTask<String, Void, ArrayList<Habit>>{

        @Override
        protected ArrayList<Habit> doInBackground(String... Hids) {
            verifySettings();
            String UserQuery;
            ArrayList<Habit> habits = new ArrayList<Habit>();

            //Build the query
            //String query = "{\n" +
            //" \"query\": { \"term\": {\"username\":\"" + usernames[0] + "\"} }\n" +"}";

            if (Hids[0].equals("")){
                UserQuery = Hids[0];
            }

            else{
                UserQuery = "{\"query\": {\"match\" : { \"hid\" : \"" + Hids[0] + "\" }}}";
            }


            Search search = new Search.Builder(UserQuery).addIndex(db).addType(habitType).build();

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

    public static class AddHabitEventsTask extends AsyncTask<HabitEvent, Void, Void> {

        @Override
        protected Void doInBackground(HabitEvent... habitEvents) {

            verifySettings();

            for (HabitEvent habitEvent : habitEvents) {
                Index index = new Index.Builder(habitEvent).index(db).type(habitEventType).build();

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

    public static class GetAttrsTask extends AsyncTask<String, Void, ArrayList<Attributes>>{

        @Override
        protected ArrayList<Attributes> doInBackground(String... attrsToFind) {
            verifySettings();

            ArrayList<Attributes> attrs = new ArrayList<>();

            Search search = new Search.Builder(attrsToFind[0]).addIndex(db).addType(attrType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Attributes> foundAttrs = result.getSourceAsObjectList(Attributes.class);
                    attrs.addAll(foundAttrs);

                } else {
                    Log.i("Error2", "Something went wrong when we tried to communicate with the elasticsearch server");
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return attrs;
        }
    }

    //Get HabitEvent by UID
    public static class GetHabitEventsByUidTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... Uids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            Log.i("Debug", Uids[0]);

            String query = "{\"query\": {\"match\" : { \"uid\" : \"" + Uids[0] + "\" }}}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Log.i("DeBug", "found some habit events");
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

    //Get HabitEvent by UID
    public static class GetHabitEventsByHidTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... Hids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            Log.i("Debug", Hids[0]);

            String query = "{\"query\": {\"match\" : { \"hid\" : \"" + Hids[0] + "\" }}}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Log.i("DeBug", "found some habit events");
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

    public static class GetMaxUidTask extends AsyncTask<Void, Void, Integer>{
        Integer maxUid = 0;
        @Override
        protected Integer doInBackground(Void... voids){

            String query = "{\n" +
                    "  \"filter\" : {\n" +
                    "    \"match_all\" : { }\n" +
                    "  },\n" +
                    "  \"sort\": [\n" +
                    "    {\n" +
                    "      \"uid\": {\n" +
                    "        \"order\": \"desc\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"size\": 1\n" +
                    "}";

            Search search = new Search.Builder(query).addIndex(db).addType(userType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    UserAccount foundUser = result.getSourceAsObject(UserAccount.class);
                    maxUid = foundUser.getUID();

                } else{
                    return 0;
                }
            }
            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return ++maxUid;
        }
    }

    public static class GetMaxHidTask extends AsyncTask<Void, Void, Integer>{
        Integer maxHid = 0;

        @Override
        protected Integer doInBackground(Void... voids){

            String query = "{\n" +
                    "  \"filter\" : {\n" +
                    "    \"match_all\" : { }\n" +
                    "  },\n" +
                    "  \"sort\": [\n" +
                    "    {\n" +
                    "      \"hid\": {\n" +
                    "        \"order\": \"desc\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"size\": 1\n" +
                    "}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Habit foundHabit = result.getSourceAsObject(Habit.class);
                    maxHid = foundHabit.getHID();

                }else{
                    return 0;
                }
            }

            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return ++maxHid;
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
