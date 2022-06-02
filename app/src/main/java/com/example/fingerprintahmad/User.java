package com.example.fingerprintahmad;

public class User {
    public String NamaLengkap, Email, Password;
    public Integer Id;

    public User(){

    }

    public User(String namaLengkap, String email, String password, Integer id){
        this.NamaLengkap = namaLengkap;
        this.Email = email;
        this.Password = password;
        this.Id = id;
    }

}
