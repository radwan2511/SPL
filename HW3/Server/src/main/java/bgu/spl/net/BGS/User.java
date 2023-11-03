package main.java.bgu.spl.net.BGS;


import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class User {
    private Integer connectionId;
    private String username;
    private String password;
    private boolean isLoggedin;
    private int birthYear;
    private int numOfPosts;
    private int numOfFollowers;
    private int numOfFollowing;
    private LocalDate birthday;

    public User(Integer conId,String name,String pass,String birth){
        this.connectionId=conId;
        this.username=name;
        this.password=pass;
        String[]date=birth.split("-");
        birthday=LocalDate.of(Integer.valueOf(date[2]),Integer.valueOf(date[1]),Integer.valueOf(date[0]));
        birthYear=Integer.valueOf(date[2]);
        isLoggedin=false;
        numOfPosts=0;
        numOfFollowers=0;
        numOfFollowing=0;
    }
    
    public void setConnectionId(Integer connectionId) {
    	this.connectionId = connectionId;
    }
    
    public void login(){
        isLoggedin=true;
    }
    public void logout(){
        isLoggedin=false;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public boolean isLoggedin() {
        return isLoggedin;
    }
    public int getAge(){
        return Period.between(birthday,LocalDate.now()).getYears();
    }
    public void post(){
        numOfPosts++;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public void follow(){
        numOfFollowing++;
    }
    public void unfollow(){
        numOfFollowing--;
    }

    public void followedByOther(){
        numOfFollowers++;
    }
    public void unfollowedByOther(){
        numOfFollowers--;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    @Override
    public String toString() {
        String s="";
        s+=String.valueOf(getAge());
        s+=" ";
        s+=String.valueOf(numOfPosts);
        s+=" ";
        s+=String.valueOf(numOfFollowers);
        s+=" ";
        s+=String.valueOf(numOfFollowing);
        return s;
    }
}
