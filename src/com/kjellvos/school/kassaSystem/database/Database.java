package com.kjellvos.school.kassaSystem.database;

import com.kjellvos.school.kassaSystem.Main;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.naming.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by kjevo on 3/29/17.
 */
@SuppressWarnings("Duplicates")
public class Database {
    Main main;
    BasicDataSource basicDataSource;
    Connection connection;
    PreparedStatement preparedStatement;

    public Database(Main main) throws SQLException {
        this.main = main;

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        System.setProperty(Context.PROVIDER_URL, "file:///tmp");

        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("KassaSystem");
        basicDataSource.setPassword("password123321");
        basicDataSource.setUrl("jdbc:mysql://213.154.224.189/KassaSystem?useSSL=false&reconnect=true&allowMultiQueries=true");

        connection = basicDataSource.getConnection();
    }
}