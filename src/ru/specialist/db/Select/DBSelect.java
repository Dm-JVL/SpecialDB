package ru.specialist.db.Select;

import java.sql.*;
import java.util.Scanner;

/**
 * Пример взаимодействия с БД MySQL
 */

public class DBSelect {

    /**
     * Объявление константных значений для сохранения информации о драйвере БД и соединении
     */
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/web?user=root&password=demo";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);

        /**
         * Открываем соединение
         */
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            // BAD (SQL INJECTION)
            // ' AND
			/*String sql = "SELECT title, length FROM courses "
					+ "WHERE title LIKE '%"+search+"%' "
					+ "ORDER BY title";
			Statement cmd = conn.createStatement();
			ResultSet result = cmd.executeQuery(sql);*/
            CallableStatement sp = conn.prepareCall("call countCourses(?)"); //хранимая процедура

            // IN, INOUT
            // sp.set..
            sp.execute(); //выполнение хранимой процедуры
            // OUT, INOUT
            int count = sp.getInt(1);

            System.out.printf("Всего курсов: %d\n", count);


            System.out.print("Поиск: ");
            Scanner sc = new Scanner(System.in);
            String search = sc.nextLine().trim();


            String sql = "SELECT title, length FROM courses "
                    + "WHERE title LIKE ? "
                    + "ORDER BY title";

            PreparedStatement cmd = conn.prepareStatement(sql);
            //cmd.setFetchSize(0);
            cmd.setString(1, "%" + search + "%");
            ResultSet result = cmd.executeQuery();

            // cmd.set..
            // cmd.executeUpdate(); // INSERT, DELETE, UPDATE


            while (result.next()) {
                String title = result.getString("title");
                // if (result.getString("length") == null)

                int length = result.getInt("length");
                if (result.wasNull())
                    System.out.printf("%-40s\n", title);
                else
                    System.out.printf("%-40s  %d\n", title, length);
            }
            result.close();
        } // conn.close()
    }
}
