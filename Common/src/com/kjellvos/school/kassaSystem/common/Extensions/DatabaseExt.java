package com.kjellvos.school.kassaSystem.common.Extensions;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.naming.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by kjevo on 3/29/17.
 */
public class DatabaseExt {
    private BasicDataSource basicDataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;


    public DatabaseExt() throws SQLException{
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        System.setProperty(Context.PROVIDER_URL, "file:///tmp");

        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("KassaSystem");
        basicDataSource.setPassword("password123321");
        basicDataSource.setUrl("jdbc:mysql://213.154.224.189/KassaSystem?useSSL=false&reconnect=true&allowMultiQueries=true");

        connection = basicDataSource.getConnection();
    }

    public BasicDataSource getBasicDataSource() {
        return basicDataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
