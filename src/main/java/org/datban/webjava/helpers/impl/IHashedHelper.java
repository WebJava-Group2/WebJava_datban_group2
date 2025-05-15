package org.datban.webjava.helpers.impl;

public interface IHashedHelper {
  boolean isPasswordValid(String password, String hashedPassword);
  String hashPassword(String password);
}
