package com.example.societymanagmentsystem;

public class mydata {

    private int id;
    private String name;
    private String email;
    private String phoneno;
    private String image;
    private String role;
    private boolean expanded;


    public mydata(String name,String phoneno,String email,String role ,String image ) {

        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.image=image;
        this.role=role;
        this.expanded=false;

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


    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }
}
