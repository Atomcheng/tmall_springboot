package jjc.springboot1.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryDB {
    public static void main(String[] args) throws ClassNotFoundException{
        Class driver = Class.forName("com.mysql.jdbc.Driver");  //加载驱动类

        String sql = "INSERT INTO category(id, name) VALUES(null, ?)";

        try(Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmall_springboot1?characterEncoding=UTF-8", "root",
                "admin");
            PreparedStatement ps = c.prepareStatement(sql);) {
            //添加10条分类数据
            for (int i = 0; i < 20; i++) {
                ps.setString(1, "测试分类" + i);
                ps.execute();
            }
            System.out.println("添加分类数据完毕");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
