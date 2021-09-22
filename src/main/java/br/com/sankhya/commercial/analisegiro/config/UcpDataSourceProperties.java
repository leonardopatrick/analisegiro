package br.com.sankhya.commercial.analisegiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="datasource.ucp")
public class UcpDataSourceProperties {

    private String url;
    private String user;
    private String password;
    private String connectionFactoryClassName;
    private int connectionWaitTimeout;
    private int inactiveConnectionTimeout;
    private int initialPoolSize;
    private int minPoolSize;
    private int maxPoolSize;
    private Boolean validateConnectionOnBorrow;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionFactoryClassName() {
        return connectionFactoryClassName;
    }

    public void setConnectionFactoryClassName(String connectionFactoryClassName) {
        this.connectionFactoryClassName = connectionFactoryClassName;
    }

    public int getConnectionWaitTimeout() {
        return connectionWaitTimeout;
    }

    public void setConnectionWaitTimeout(int connectionWaitTimeout) {
        this.connectionWaitTimeout = connectionWaitTimeout;
    }

    public int getInactiveConnectionTimeout() {
        return inactiveConnectionTimeout;
    }

    public void setInactiveConnectionTimeout(int inactiveConnectionTimeout) {
        this.inactiveConnectionTimeout = inactiveConnectionTimeout;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Boolean getValidateConnectionOnBorrow() {
        return validateConnectionOnBorrow;
    }

    public void setValidateConnectionOnBorrow(Boolean validateConnectionOnBorrow) {
        this.validateConnectionOnBorrow = validateConnectionOnBorrow;
    }
}