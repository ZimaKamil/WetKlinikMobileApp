package com.example.asztar.wetklinikmobileapp;

public class Token {
    private static String access_token;
    private static String token_type;
    private static String userName;
    private static String expires_in;
    private static Token single_instance = null;

    private Token() {
    }

    public void setAccess_token(String access_token) {
        Token.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setExpires_in(String expires_in) {
        Token.expires_in = expires_in;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setToken_type(String token_type) {
        Token.token_type = token_type;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setUserName(String userName) {
        Token.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public static Token getInstance() {
        if (single_instance == null)
            synchronized (Token.class) {
                if (single_instance == null)
                    single_instance = new Token();
            }
        return single_instance;
    }

    public static void reset() {
        single_instance = new Token();
    }
}
