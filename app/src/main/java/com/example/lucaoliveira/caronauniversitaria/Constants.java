package com.example.lucaoliveira.caronauniversitaria;

/**
 * Created by lucaoliveira on 6/19/2016.
 */
public class Constants {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;
    public static final int STATUS_ERROR = 400;
    public static final int STATUS_UNAUTHORIZED = 401;

    //Chave e senha que vem do servidor para acessar API
    public static final String APP_KEY = "TBA";
    public static final String APP_SECRET = "TBA";

    //URL para acessar API
    public static final String ENDPOINT = "YOURDOMAIN/api";
    public static final String LOGIN_URL = ENDPOINT + "/login.php";
    public static final String SIGNUP_URL = ENDPOINT + "/signup.php";
    public static final String INFO_URL = ENDPOINT + "/info.php";
    public static final String UPDATE_URL = ENDPOINT + "/update.php";
    public static final String DELETE_URL = ENDPOINT + "/delete.php";
    public static final String RESET_URL = ENDPOINT + "/reset.php";


    //Constantes usadas no parsing JSON ou valores anexados em uma conexão URL com servidor
    public static final String AUTHORIZATION = "Authorization";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_CREDENTIAL = "client_credentials";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS = "access";
    public static final String INFO = "info";
    public static final String STATUS = "status";
    public static final String MESSAGE = "msg";
    public static final String ID = "id";
    public static final String ID_INFO = "id";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String NOTE = "note";
    public static final String NAME = "name";

    public static final String CONNECTION_MESSAGE = "No internet Connection";

}
