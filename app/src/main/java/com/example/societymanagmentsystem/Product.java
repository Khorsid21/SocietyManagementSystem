package com.example.societymanagmentsystem;

public class Product {
    private int id;
    private String name;
    private String email;
    private String phoneno;
    private String imageUrl;

    public Product(String name, String email, String phoneno)
    {

        this.name = name;
        this.email = email;
        this.phoneno = phoneno;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneno() {
        return phoneno;
    }



}
