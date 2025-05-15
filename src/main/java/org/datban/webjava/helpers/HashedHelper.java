package org.datban.webjava.helpers;

import org.datban.webjava.helpers.impl.IHashedHelper;
import org.mindrot.jbcrypt.BCrypt;

public class HashedHelper implements IHashedHelper {
  @Override
  public boolean isPasswordValid(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
