package com.github.liuyehcf.framework.rpc.ares.readme;

import com.github.liuyehcf.framework.rpc.ares.AresMethod;
import com.github.liuyehcf.framework.rpc.ares.AresRequestParam;

public interface UserService {

    @AresMethod(path = "/user/get")
    UserInfo getUser(@AresRequestParam(name = "id") int id);

    class UserInfo {
        private Integer id;
        private String firstName;
        private String lastName;
        private Integer age;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
