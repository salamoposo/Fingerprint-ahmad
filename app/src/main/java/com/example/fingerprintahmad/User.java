package com.example.fingerprintahmad;

public class User {
    public String NamaLengkap, Email, Password;
    public Integer Id;

    public User() {

    }

    public User(String nama, String email, Integer id, String password) {
        this.NamaLengkap = nama;
        this.Email = email;
        this.Password = password;
        this.Id = id;
    }

}
