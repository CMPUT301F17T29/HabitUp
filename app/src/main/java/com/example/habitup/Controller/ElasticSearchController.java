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

import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
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
                Index index = new Index.Builder(user).index(db).type(userType).refresh(true).id(Integer.toString(user.getUID())).build();

                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.i("HabitUpDEBUG", "AddUsersTask Success");
                    }

                    else{
                        Log.i("Error", "Elasticsearch was not able to add the User");
                        throw new RuntimeException("Failed to add user.");
                    }
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the User");
                    throw new RuntimeException("Failed to add user.");
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

    /* Given a username, returns the user object corresponding to it */
    public static class GetAllUsers extends AsyncTask<Void, Void, ArrayList<UserAccount>> {
        @Override
        protected ArrayList<UserAccount> doInBackground(Void... voids) {
            verifySettings();

            ArrayList<UserAccount> accounts = new ArrayList<>();
            String UserQuery;

            UserQuery = "{\"query\": {\"match_all\" : {}}}";

            String query = "{" +
                                "\"size\": " + HabitUpApplication.NUM_OF_ES_RESULTS + "," +
                                "\"from\": 0," +
                                "\"query\": {" +
                                    "\"match_all\" : {}" +
                                "}" +
                            "}";


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
                Index index = new Index.Builder(attr).index(db).type(attrType).id(Integer.toString(attr.getUid())).refresh(true).build();


                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.i("HabitUpDEBUG", "AddAttr SUCCESS");
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

    public static class GetAttributesTask extends AsyncTask<String, Void, ArrayList<Attributes>> {
        @Override
        protected ArrayList<Attributes> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Attributes> attributes = new ArrayList<>();
            String query;

            if (search_parameters[0].equals("")){
                query = search_parameters[0];
            }

            else{
                query = "{\"query\": {\"match\" : { \"uid\" : \"" + search_parameters[0] + "\" }}}";
            }

            //Log.i("Debug", "username to search for is: "+ search_parameters[0]);

            Search search = new Search.Builder(query)
                    .addIndex(db)
                    .addType(attrType)
                    .build();

            try {
                // Send request to the server to get the user
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){

                    List<Attributes> foundAttr = result.getSourceAsObjectList(Attributes.class);
                    attributes.addAll(foundAttr);
                }
                else{
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return attributes;
        }
    }

    public static class AddHabitsTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits) {

            verifySettings();

            for (Habit habit : habits) {
                Index index = new Index.Builder(habit).index(db).type(habitType).id(Integer.toString(habit.getHID())).refresh(true).build();


                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.i("HabitUpDEBUG", "Wrote Habit " + habit.getHabitName());
                    } else {
                        Log.i("HabitUpDEBUG", "Elasticsearch was not able to add the Habit");
                    }

                } catch (Exception e) {
                    Log.i("HabitUpDEBUG", "The application failed to build and send the Habit");
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

    public static class GetUserHabitsTask extends AsyncTask<String, Void, ArrayList<Habit>>{

        @Override
        protected ArrayList<Habit> doInBackground(String... uids) {
            verifySettings();
            String UserQuery;
            ArrayList<Habit> habits = new ArrayList<Habit>();

            if (uids[0].equals("")){
                UserQuery = uids[0];
            }

            else {
                UserQuery = "{" +
                                "\"size\": " + HabitUpApplication.NUM_OF_ES_RESULTS + "," +
                                "\"from\": 0," +
                                "\"query\": {" +
                                    "\"match\" : " +
                                        "{ \"uid\" : \"" + uids[0] + "\" }" +
                                "}" +
                            "}";
            }

//            UserQuery = "{\"query\": {\"match\" : { \"uid\" : \"" + uids[0] + "\" }}}";
//              }

            Search search = new Search.Builder(UserQuery).addIndex(db).addType(habitType).build();

            try {

                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Log.i("HabitUpDEBUG", "Got Habits from ES");

                    List<Habit> foundHabit = result.getSourceAsObjectList(Habit.class);

//                    for (Habit habit: foundHabit) {
//                        Log.i("HabitUpDEBUG", "Habit Got: " + habit.getHabitName());
//                    }
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
                Index index;
                if (habitEvent.getEID() == null) {
                    index = new Index.Builder(habitEvent).index(db).type(habitEventType).refresh(true).build();
                } else {
                    index = new Index.Builder(habitEvent).index(db).type(habitEventType).id(habitEvent.getEID()).refresh(true).build();
                }

                try {

                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.i("HabitUpDEBUG", "AddHabitEventTask getId: " + result.getId());

                        if (habitEvent.getEID() == null) {
                            habitEvent.setEID(result.getId());
                            Index reindex = new Index.Builder(habitEvent).index(db).type(habitEventType).id(result.getId()).build();
                            try {
                                DocumentResult newResult = client.execute(reindex);
                                if (newResult.isSucceeded()) {
                                    Log.i("HabitUpDEBUG", "Update EID OK");
                                } else {
                                    Log.i("HabitUpDEBUG", "Update EID failed.");
                                }
                            } catch (Exception e) {
                                Log.i("HabitUpDEBUG", "Exception-ception!!");
                            }
                        }
                    }
                    else{
                        Log.i("Error", "Elasticsearch was not able to add the Habit Event");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the Habit Event");
                }
            }

            return null;
        }
    }

    //Get HabitEvent by EID
    public static class GetHabitEventsByEIDTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... eids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            String query = "{\"query\": {\"match\" : { \"eid\" : \"" + eids[0] + "\" }}}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

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

    //Get HabitEvent by UID
    public static class GetHabitEventsByUidTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... Uids) {

            Log.i("HabitUpDEBUG", "GetHabitEventsByUidTask");
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

//            Log.i("HabitUpDebug", "Getting Habits for UID " + Uids[0]);

            String query = "{" +
                                "\"size\": " + HabitUpApplication.NUM_OF_ES_RESULTS + "," +
                                "\"from\": 0," +
                                "\"query\": {" +
                                    "\"match\" : " +
                                        "{ \"uid\" : \"" + Uids[0] + "\" }" +
                                "}" +
                            "}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

            try {

                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Log.i("DeBug", "found some habit events");
                    List<HabitEvent> foundHabitEvent = result.getSourceAsObjectList(HabitEvent.class);
                    habitEvents.addAll(foundHabitEvent);

                } else {
                    Log.i("HabitUpDEBUG", "ESCtrl/GetHabitsByUID - result failed");
                }
            }
            catch (Exception e) {
                Log.i("HabitUpDEBUG", "ESCtrl/GetHabitsByUID - exception");
            }

            return habitEvents;
        }
    }

    // Get HabitEvent by UID
    public static class GetHabitEventsByHidTask extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... Hids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            Log.i("Debug", Hids[0]);

//            String query = "{\"query\": {\"match\" : { \"hid\" : \"" + Hids[0] + "\" }}}";

            String query = "{" +
                                "\"size\": " + HabitUpApplication.NUM_OF_ES_RESULTS + "," +
                                "\"from\": 0," +
                                "\"query\": {" +
                                    "\"match\" : " +
                                        "{ \"hid\" : \"" + Hids[0] + "\" }" +
                                "}" +
                            "}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

            try {

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

    // Get HabitEvent by UID
    public static class GetHabitEventsForDelete extends AsyncTask<String, Void, ArrayList<HabitEvent>>{

        @Override
        protected ArrayList<HabitEvent> doInBackground(String... Hids) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();

            String query = "{" +
                    "\"size\": " + HabitUpApplication.NUM_OF_ES_RESULTS_FOR_DELETE + "," +
                    "\"from\": 0," +
                    "\"query\": {" +
                    "\"match\" : " +
                    "{ \"hid\" : \"" + Hids[0] + "\" }" +
                    "}" +
                    "}";

            Search search = new Search.Builder(query).addIndex(db).addType(habitEventType).build();

            try {

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

                } else {
                    return 0;
                }
            }

            catch (Exception e) {
                Log.i("Error1", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return ++maxHid;
        }

    }

    //given a UID, deletes the corresponding user
    public static class DeleteUserTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... uids) {
            verifySettings();

            for (String uid : uids) {

                String query = "{\"query\": {\"match\" : { \"uid\" : \"" + uid + "\" }}}";

                DeleteByQuery deleteUser = new DeleteByQuery.Builder(query).addIndex(db).addType(userType).build();

                try {
                    // where is the client?
                    client.execute(deleteUser);
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build the query and delete the User");

                }
            }

            return null;
        }
    }

    //given an HID for a habit, delete the corresponding habit
    public static class DeleteHabitTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... hids) {
            verifySettings();

            for (String hid : hids) {

                String query = "{\"query\": {\"match\" : { \"hid\" : \"" + hid + "\" }}}";

                DeleteByQuery deleteHabit = new DeleteByQuery.Builder(query).addIndex(db).addType(habitType).build();

                try {
                    // where is the client?
                    client.execute(deleteHabit);
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build the query and delete the User");

                }
            }

            return null;
        }
    }

    //Given a ESid for a habit event, dletes the corresponding event from ES
    public static class DeleteHabitEventTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... ESids) {
            verifySettings();

            Log.i("HabitUpDEBUG", "ESCtl/Trying to delete " + ESids[0]);

            for (String Esid : ESids) {

                Delete deleteHabit = new Delete.Builder(Esid).index(db).type(habitEventType).build();

                try {
                    client.execute(deleteHabit);
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build the query and delete the User");

                }
                Log.i("HabitUpDEBUG", "ESCtl/Deleted " + ESids[0]);
            }

            return null;
        }
    }



    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(serverURL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
