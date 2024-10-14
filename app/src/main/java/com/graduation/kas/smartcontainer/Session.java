package com.graduation.kas.smartcontainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    String phone;
    String usertype;
    String name;
    String email;
    String username;
    String id;


    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;




    public String getUsername() {
        return sp.getString("username", username);
    }

    public void setUsername(String username) {
        this.username = username;
        spEditor=sp.edit();
        spEditor.putString("username", username);
        spEditor.commit();
    }


    public String getEmail() {
        return sp.getString("email", email);
    }

    public void setEmail(String email) {
        this.email = email;
        spEditor=sp.edit();
        spEditor.putString("email", email);
        spEditor.commit();
    }




    public void setName(String name)
    {
        this.name =name;
        spEditor=sp.edit();
        spEditor.putString("name", name);
        spEditor.commit();
    }
    public String getName()
    {
        return sp.getString("name", name);
    }


    public Session(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public void setPhone(String phone)
    {
        this.phone = phone;
        spEditor=sp.edit();
        spEditor.putString("phone", phone);
        spEditor.commit();
    }
    public String getPhone()
    {
        return sp.getString("phone", phone);
    }

    public void setId(String id)
    {
        this.id=id;
        spEditor=sp.edit();
        spEditor.putString("id",id);
        spEditor.commit();
    }
    public  String getId()
    {
        return sp.getString("id",id);
    }

    public void setUsertype(String usertype)
    {
        this.usertype = usertype;
        spEditor=sp.edit();
        spEditor.putString("role", usertype);
        spEditor.commit();
    }

    public String getUsertype()
    {
        return sp.getString("role", usertype);
    }

    public boolean setLogin(boolean status) {
        spEditor = sp.edit();
        spEditor.putBoolean("is_logged_in", status);
        spEditor.commit();
        return true;
    }

    public boolean getLoggedIn() {
        return sp.getBoolean("is_logged_in", false);
    }

}
