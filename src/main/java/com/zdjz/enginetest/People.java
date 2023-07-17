package com.zdjz.enginetest;

public class People {
    public String name;
    public Integer age;
    public String  ipAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public People(String name, Integer age, String ipAddress) {
        this.name = name;
        this.age = age;
        this.ipAddress = ipAddress;
    }
}
