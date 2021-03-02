package com.example.e_campusguide;

public class User
{
    String studentid,name,program,session,address,email,phone;



    User(String studentid,String name,String program,String session, String address, String email, String phone)
    {
        this.studentid=studentid;
        this.name=name;
        this.address=address;
        this.email=email;
        this.phone=phone;
        this.program=program;
        this.session=session;
    }
}
