package Lesson2;

import java.sql.*;

public class Main { //создаем "включатели" и "выключатели" нашей базы
    private static Statement stmt;
    private static Connection connection;
    private static PreparedStatement pstmt;
int a =2;
    public static void connect(){ //подключаем базу
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void disconnect(){    //отключаем приложение (базу)
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) { //здесь всё реализуется (старт работы начинается здесь)

        connect();

        try { // извлечение информации из таблицы (в базе)
            ResultSet rs = stmt.executeQuery("SELECT * FROM students"); //прикрепляем нашу таблицу из бд (rs) - имя
            while (rs.next()){  //проходимся по всей таблице от самого первого элемента, до последнего
                System.out.println(rs.getInt(1) + " " + rs.getString("name")+ " " + rs.getString("score"));
            } //вывод данных таблицы в консоль
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//----------------------------------------------------------------------------------------------------------------------

        try { // получение информации о самой таблице, например: кол-во столбцов, строк и т.д.
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++){
                System.out.println(rsmd.getColumnName(i) + " " + rsmd.getColumnType(i));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//----------------------------------------------------------------------------------------------------------------------

        try { //вносим нового студента в таблицу (в базе)
            int res = stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Adriana', 50)");
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }

//----------------------------------------------------------------------------------------------------------------------

        try {// уничтожение таблицы в базе
            int res = stmt.executeUpdate("DROP TABLE IF EXISTS students");
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }

//----------------------------------------------------------------------------------------------------------------------

        try {// создание новой таблицы в базе
            stmt.executeUpdate("CREATE TABLE students (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name TEXT," +
                    " score INTEGER)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }

//----------------------------------------------------------------------------------------------------------------------

        try {// добавление большого колличества эллементов в таблицу. Занимает очень много времени.
            //конструкции №1 и №2 ускоряют процесс.
            long t = System.currentTimeMillis(); //замеряем время
            connection.setAutoCommit(false); // №1
            for (int i = 0; i < 1000; i++){
                stmt.executeUpdate("INSERT INTO students (name, score) VALUES ('test_name', 100)");
            }
            connection.setAutoCommit(true); // №2
            System.out.println(System.currentTimeMillis() - t);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
       }

//----------------------------------------------------------------------------------------------------------------------

        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement("INSERT INTO students (name, score) VALUES (?, ?);");
            for (int i = 0; i < 1000; i++) {
            pstmt.setString(1, "Adri" + i);
            pstmt.setInt(2, i);
            pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }



        try {
            connection.setAutoCommit(false);
            stmt.executeUpdate("INSERT INTO students(name, score) VALUES ('Bob1',10)");
            Savepoint sp =connection.setSavepoint();
            stmt.executeUpdate("INSERT INTO students(name, score) VALUES ('Bob2',20)");
            connection.rollback(sp);
            connection.commit();
            stmt.executeUpdate("INSERT INTO students(name, score) VALUES ('Bob3',30)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }


    }
}
