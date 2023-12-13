package database;

import java.sql.*;


import region.Region;
import user.User;

public class Database {
    
    Statement stml = null;
    Connection c = null;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bils.db");
            c.setAutoCommit(false);
            stml = c.createStatement();
        } catch (Exception e) {
            c = null;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
    }
    private String getId() {
        int id = 10001;
        try {
            ResultSet values = stml.executeQuery("SELECT * FROM USERS;");
            while (values.next()) {
                id += 1;
            }
            return Integer.toString(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }
    public int addUser(String name, String password) {
        try {
            String id = this.getId();
            String query = String.format("Insert into users values('%s','%s','%s');", Integer.parseInt(id), name, password);
            stml.executeUpdate(query);
            c.commit();
            return Integer.parseInt(id);
        } catch (SQLException e) {
            return 0;
        }
    }
    public int addbills(int id,String month, String region,int units,int amount) {
        try {
            String query = String.format("Insert into billamount values('%s','%s','%s','%s','%s');", id,month,region,units,amount);
            stml.executeUpdate(query);
            c.commit();
            return 1;
        } catch (SQLException e) {
            return 0;
        }
    }
      public User getUser(String username) {
        try {
            String query = String.format("Select * from users where username = '%s';", username);
            ResultSet res = stml.executeQuery(query);
            int id = Integer.parseInt(res.getString(1));
            String name= res.getString(2);
            String pass= res.getString(3);
            String query1 = String.format("Select * from billamount where id = '%s';", id);
            ResultSet res1 = stml.executeQuery(query1);
            String month= res1.getString(2);
            String region= res1.getString(3);
            int units = Integer.parseInt(res1.getString(4));
            User user  = new User(name, pass, 12, 4, units);
            user.storeData(month, units, new Region(region, 8));
            return user;
        } catch (SQLException e) {
            return null;
        }
    }
    public User[] allUsers(){
                try {
            String query = String.format("Select * from users;");
            User[] users = new User[100];
            ResultSet res = stml.executeQuery(query);
            int i=  0;
            while(res.next()){
                int id = Integer.parseInt(res.getString(1));
                String name= res.getString(2);
                String pass= res.getString(3);
                String query1 = String.format("Select * from billamount where id = '%s'", id);
                ResultSet res1 = stml.executeQuery(query1);
                User user  = new User(name, pass, 12, 4, id);
                users[i] = user;
                i++;
                while (res1.next()) {
                    String month= res1.getString(2);
                    String region= res1.getString(3);
                    System.out.println(res1.getString(4));
                    int units = Integer.parseInt(res1.getString(4));
                    user.storeData(month, units, new Region(region, 8));
                }
            }
            return users;
        } catch (SQLException e) {
            return null;
        }
    }
}