package com.test.util;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptExample {
    public static void main(String[] args) {
        String password = "123456";

        System.out.println("BCrypt.gensalt() = " + BCrypt.gensalt());
        System.out.println("BCrypt.gensalt() = " + BCrypt.gensalt());

        // 生成哈希值，使用默认的工作因子（10）
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed Password: " + hashedPassword);
        String hashedPassword2 = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed Password2: " + hashedPassword2);

        // 验证密码
        boolean isPasswordCorrect = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Password is correct: " + isPasswordCorrect);
        boolean isPasswordCorrect2 = BCrypt.checkpw(password, hashedPassword2);
        System.out.println("Password is correct: " + isPasswordCorrect2);
    }
}
