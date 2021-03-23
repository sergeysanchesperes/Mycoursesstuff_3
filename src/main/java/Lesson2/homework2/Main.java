package Lesson2.homework2;

import java.sql.*;

public class Main { //создаем "включатели" и "выключатели" нашей базы
    private static Statement stmt;
    private static Connection connection;
    private static PreparedStatement pstmt;

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
    public static void createtable(){
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
    }
    public static void addnewinfo(){
        try { //вносим нового студента в таблицу (в базе)
            int res = stmt.executeUpdate("INSERT INTO students (name, score) VALUES('Adriana', 50)");
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }
    }
    public static void getinfo(){
        try { // извлечение информации из таблицы (в базе)
            ResultSet rs = stmt.executeQuery("SELECT * FROM students"); //прикрепляем нашу таблицу из бд (rs) - имя
            while (rs.next()){  //проходимся по всей таблице от самого первого элемента, до последнего
                System.out.println(rs.getInt(1) + " " + rs.getString("name")+ " " + rs.getString("score"));
            } //вывод данных таблицы в консоль
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deleteinfo(){
        try { //удаление информации в таблице
            stmt.executeUpdate("DELETE FROM students");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deletetable(){
        try {// уничтожение таблицы в базе
            int res = stmt.executeUpdate("DROP TABLE IF EXISTS students");
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            disconnect();
        }
    }

    public static void main(String[] args) { 

        connect();
        createtable();
        addnewinfo();
        getinfo();
        deleteinfo();
        deletetable();



    }
}


