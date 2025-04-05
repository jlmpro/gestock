package com.mystock.mygestock.interceptor;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class Interceptor implements StatementInspector {
    @Override
    public String inspect(String s) {
        System.out.println("jpa sql:" + s);
        return s;
    }
}
