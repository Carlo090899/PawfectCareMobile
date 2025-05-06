package com.example.pawfectcareapp.API;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor{

    private String authToken;
    private String authName;

    public AuthenticationInterceptor(String auth_name, String token) {
        this.authName = auth_name;
        this.authToken = token;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header(authName, authToken);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
