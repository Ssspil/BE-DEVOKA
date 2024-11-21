package com.jspp.devoka.db.connection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class PostgreSQLConnectionTest {

    private String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=devoka";
    private String USERNAME = "postgres";
    private String PASSWORD = "0000";


//    @Test
//    @DisplayName("DB 연결테스트")
//    public void ConnectionTest() throws Exception{
//        Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD); //db 연결
//        Assertions.assertThat(con).isNotNull();
//    }
//
//    @Test
//    @DisplayName("DB 연결 및 카테고리 ID 검증")
//    public void ConnectionNamePrintTest() throws Exception {
//        // Given
//        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//             Statement pre = con.createStatement();
//             ResultSet rs = pre.executeQuery("SELECT * FROM category_info WHERE category_id = 'A0001'")) {
//
//            // When
//            String name = null;
//            if (rs.next()) {
//                name = rs.getString("category_name");
//            }
//
//            // Then
//            Assertions.assertThat(name).isEqualTo("개발");
//        } catch (SQLException e) {
//            // 예외 처리
//            Fail.fail("테스트에 예외가 발생했습니다: " + e.getMessage());
//        }
//    }
}
