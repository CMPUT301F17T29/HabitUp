package com.example.habitup.Model;

import java.io.File;

/**
 * Created by Ty on 2017-10-21.
 *
 * Represents the app instance
 *
 */

public class HabitUp {

    //Members

    private UserAccountList users;
    private File localData;
    private Boolean elasticConnectivity;
    private UserAccount currentUser;

    public HabitUp(){
        //TODO implement boundary checking

        users = new UserAccountList();
        currentUser = null;
        this.checkConnectivity();
        this.loadFile();
    }

    public UserAccountList getUsers(){
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

    public void setUsersList(UserAccountList users_){
        this.users = users_;
    }

    public void setLocalData(File localData_){
        this.localData = localData_;
    }

    public void setElasticConnectivity(boolean x){
        this.elasticConnectivity = x;
    }

    public void setCurrentUser(UserAccount currentUser_){
        this.currentUser = currentUser_;
    }

    /**
     * grants the user access to their account
     */
    public void login(String username)throws IllegalArgumentException {

    }

    /**
     * Checks elastic connectivity and sets the value for HabitUp instance
     */
    public void checkConnectivity(){
        //check connectivity
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
