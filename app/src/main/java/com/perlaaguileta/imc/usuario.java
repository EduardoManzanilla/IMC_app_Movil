package com.perlaaguileta.imc;

public class usuario {
    //variable de los datos a registrar

    private String name ;
    private int age;
    private double weight;
    private double height;
    private String email;
    private String password;

    public usuario(String name, int age, double weight, double height, String email, String password) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.email = email;
        this.password = password;
    }

    public usuario() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "usuario{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
