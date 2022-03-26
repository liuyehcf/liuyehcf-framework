package com.github.liuyehcf.framework.rpc.ares.test.model;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
public class Person {
    private String country;
    private String name;
    private int age;

    public Person() {
    }

    public Person(String country, String name, int age) {
        this.country = country;
        this.name = name;
        this.age = age;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public static final class Builder {
        private String country;
        private String name;
        private int age;

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Person build() {
            return new Person(country, name, age);
        }
    }
}
