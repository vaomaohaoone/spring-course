package ru.otus.spring.course;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public abstract class JdbcTest {
    @Autowired
    protected EntityUtils entityUtils;

    @Autowired
    private DataSource dataSource;

    @AfterEach
    protected void clenupDB() throws SQLException {
        handleTables(tableName -> "TRUNCATE TABLE \"" + tableName + "\"");
    }

    private void handleTables(Function<String, String> sqlMapper) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS false");
            conn.setAutoCommit(false);
            Statement selectStm = conn.createStatement();
            Statement executeStm = conn.createStatement();
            ResultSet resultSet = selectStm.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                executeStm.execute(sqlMapper.apply(tableName));
            }
            conn.commit();
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS true");
        }
    }

}
