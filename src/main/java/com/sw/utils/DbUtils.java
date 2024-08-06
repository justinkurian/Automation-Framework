package com.sw.utils;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


@Slf4j
public class DbUtils {

    // JDBC URL, username, and password for Azure SQL Database
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://sqlsrv-wwin-tst-001.database.windows.net:1433;database=sqldb-wwin-tst-002;user={KURIANJU@scottishwater.co.uk};password={BarcelonaMessi10*};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;authentication=ActiveDirectoryPassword";


    public static List<Map<String,Object>> getValueFromDB(String query, String...columns){

        Connection connection = null;
        Statement statement = null;

        try {
            // Load the JDBC driver
            Class.forName(DRIVER_CLASS);

            // Establish the connection
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();

            // Example query
            ResultSet resultSet = statement.executeQuery(query);

            List<Map<String,Object>> resultList = new ArrayList<>();
                // Iterate through the ResultSet
                while (resultSet.next()) {
                    Map<String,Object> row = new HashMap<>();
                    for (String column : columns) {
                        // Add the value of the current column to the row list
                        row.put(column, resultSet.getObject(column));
                    }
                    // Add the row list to the result list
                    resultList.add(row);
                }

                log.info("DB Output for the Query => {}",resultList);
            return resultList;

        } catch (ClassNotFoundException e) {
            log.error("SQL Server JDBC Driver not found.",e);
        } catch (SQLException e) {
            log.error("SQL Exception occurred.",e);
        } finally {
            // Clean up the environment
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                log.error("SQL Exception occurred.",se);
            }
        }
        return null;
    }

}
