package com.example.habitup.Model;

import android.media.Image;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ty on 2017-10-21.
 *
 * Represents the app instance
 *
 */

public class HabitUp {

    //Members

    private ArrayList<UserAccount> users;
    private File localData;
    private Boolean elasticConnectivity;
    private UserAccount currentUser;

    public HabitUp(){
        // get array list from elastic search
        // load local data into file
        // check connectivity
    }

    public ArrayList<UserAccount> getUsers(){
        return this.users;
    }

    public File getLocalData(){
        return this.localData;
    }

    public Boolean getElasticConnectivity(){
        return this.elasticConnectivity;
    }

    public UserAccount getCurrentUser(){
        return this.currentUser;
    }

    /**
     * grants the user access to their account
     */
    public void login(String username)throws IllegalArgumentException {

    }

    /**
     * Takes a new user and adds them to the arrayList users
     * @param user
     */
    public void addNewUser(UserAccount user){

    }

    /**
     * Returns a list of Users who match the search term
     * @param searchTerm
     * @return
     */
    public ArrayList<UserAccount> searchUsers(String searchTerm){
        //placeholder
        return this.getUsers();
    }

    /**
     * Loads the local save file into
     */
    public void loadFile(){

    }

    /**
     * saves the file locally
     */
    public void saveFile(){

    }

    /**
     * Loads the data saved in ElasticSearch to localData
     */
    public void loadFileFromElastic(){

    }

    /**
     * saves local data to ElasticSearch
     */
    public void saveToElastic(){}
}
