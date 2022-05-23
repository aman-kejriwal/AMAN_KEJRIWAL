import java.sql.*;

class login_db {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    login_db() {
        try {

            // MAKE SURE YOU KEEP THE mysql_connector.jar file in java/lib folder
            // ALSO SET THE CLASSPATH
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginform", "root", "root");
            pst = con.prepareStatement("select * from login where username=? and password=?");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ip:username,password
    // return boolean
    public Boolean checkLogin(String uname, String pwd) {
        try {

            pst.setString(1, uname); // this replaces the 1st "?" in the query for username
            pst.setString(2, pwd); // this replaces the 2st "?" in the query for password
            // executes the prepared statement
            rs = pst.executeQuery();
            if (rs.next()) {
                // TRUE if the query founds any corresponding data
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating" + e);
            return false;
        }
    }
}