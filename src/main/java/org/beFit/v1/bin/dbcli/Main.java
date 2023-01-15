package org.beFit.v1.bin.dbcli;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3360/beFit","root","1234");

        DataSource dataSource = new SingleConnectionDataSource(conn, false);
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(dataSource);
        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager(dataSource);
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
    }
}
