package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class populateData {
	static Connection conn = null;

	public static void main(String[] args) {
		String url = "jdbc:mariadb://140.127.74.226:3306/411077008";
		String user = "411077008";
		String pwd = "411077008";
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(url, user, pwd);
			System.out.println("connect");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Statement st;
		try {
			st = conn.createStatement();
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`customer`")) {
				while (rs.next()) {
					System.out.println("customer : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`normal_customer`")) {
				while (rs.next()) {
					System.out.println("normal customer : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`online_customer`")) {
				while (rs.next()) {
					System.out.println("online customer : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`contract_customer`")) {
				while (rs.next()) {
					System.out.println("contract customer : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`contract`")) {
				while (rs.next()) {
					System.out.println("contract : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`card`")) {
				while (rs.next()) {
					System.out.println("card : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`order`")) {
				while (rs.next()) {
					System.out.println("order : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`product`")) {
				while (rs.next()) {
					System.out.println("product : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`store`")) {
				while (rs.next()) {
					System.out.println("store : " + rs.getString(1));
				}
			}
			try (ResultSet rs = st.executeQuery("select count(*)\n" + "from `411077008`.`warehouse`")) {
				while (rs.next()) {
					System.out.println("warehouse : " + rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}