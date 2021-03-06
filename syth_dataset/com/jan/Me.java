package com.jan;

import java.util.*;

/**
 * @author jan
 * @date 2017年11月18日 下午10:55:10
 * @version V1.0 
 */
//Me.java
public class Me {
 private Integer age;
 private String name;
 private Map<String, Object> params;
 private List<String> favoriteBooks;

 public Integer getAge() {
     return age;
 }

 public void setAge(Integer age) {
     this.age = age;
 }

 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

 public Map<String, Object> getParams() {
     return params;
 }

 public void setParams(Map<String, Object> params) {
     this.params = params;
 }

 public List<String> getFavoriteBooks() {
     return favoriteBooks;
 }

 public void setFavoriteBooks(List<String> favoriteBooks) {
     this.favoriteBooks = favoriteBooks;
 }

 @Override
 public String toString() {
     return "Me{" +
             "age=" + age +
             ", name='" + name + '\'' +
             ", params=" + params +
             ", favoriteBooks=" + favoriteBooks +
             '}';
 }
}


