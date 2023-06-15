package database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class Query {
	static Connection conn = null;
	
	public static void main(String[] args) {
		String url = "jdbc:mariadb://140.127.74.226:3306/411077008";
		String user = "411077008";
		String pwd = "411077008";
		String sql;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url,user,pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement st = conn.createStatement();
			
			Query1(st);
			
			Query2(st);
			
			Query3(st);
			
			Query4(st);
			
			conn.setAutoCommit(false); 
			Query5(st,conn);
			conn.commit();
			System.out.println("Updated data successfully");
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			if(conn != null) {
		        try {
		            conn.rollback();
		        }catch(SQLException ex) {
		            ex.printStackTrace();
		        }
		    }
		}
		
	}
	static void Query1(Statement st) {
		//Find the customer who has bought the most (by price) in the past year.
		System.out.println("Query1:");
		String sql = "SELECT    customer_id , name , total\r\n"
				+ "FROM      (SELECT    order.customer_id , name , sum(total_amount) as total , DENSE_RANK() OVER(ORDER BY total desc) as rank\r\n"
				+ "           FROM      `411077008`.`order` , `411077008`.`customer`\r\n"
				+ "           WHERE     (order.customer_id = customer.customer_id) and (order_date BETWEEN '2022/01/01' AND '2022/12/31')\r\n"
				+ "           GROUP BY  order.customer_id\r\n"
				+ "           ORDER BY  total desc , customer_id asc) as sales\r\n"
				+ "WHERE      rank < 2";
		try(ResultSet rs = st.executeQuery(sql)){
			while (rs.next()) {
				System.out.println("customer id : " + rs.getString("customer_id"));
				System.out.println("customer name : " + rs.getString("name"));
				System.out.println("total : " + rs.getString("total"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	static void Query2(Statement st) {
		//Find the top 2 products by dollar-amount sold in the past year.(here is by TWD)
		System.out.println("Query2:");
		String sql = "SELECT    product_id , type , brand , name , total\r\n"
				+ "FROM      (SELECT     product.product_id , type , brand , name , sum(total_amount) as total , DENSE_RANK() OVER(ORDER BY total desc) as rank\r\n"
				+ "           FROM       `411077008`.`product`, `411077008`.`order`\r\n"
				+ "           WHERE      order.product_id = product.product_id\r\n"
				+ "           GROUP BY   product.product_id\r\n"
				+ "           ORDER BY   total desc , product_id asc) as num\r\n"
				+ "WHERE     rank < 3";
		try(ResultSet rs = st.executeQuery(sql)){
			while (rs.next()) {
				System.out.println("product id : " + rs.getString("product_id"));
				System.out.println("type : " + rs.getString("type"));
				System.out.println("brand : " + rs.getString("brand"));
				System.out.println("name : " + rs.getString("name"));
				System.out.println("total : " + rs.getString("total"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	static void Query3(Statement st) {
		//Find those products that are out-of-stock at every store in KaoHsiung.
		System.out.println("Query3:");
		String sql = "SELECT    product.brand,product.name\r\n"
				+ "FROM      `411077008`.`store`,`411077008`.`product`\r\n"
				+ "WHERE     store_inventory = 0 and (store.product_id) = (product.product_id)\r\n"
				+ "GROUP BY  store.product_id\r\n"
				+ "HAVING    count(store.store_name) = (SELECT     count(distinct store_name)\r\n"
				+ "                                     FROM        `411077008`.store\r\n"
				+ "                                     WHERE       store_name like '%高雄%')";
		try(ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("brand : " + rs.getString("brand"));
				System.out.println("name : " + rs.getString("name"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	static void Query4(Statement st) {
		//Find those packages that were not delivered within the promised time.(here is the eta(estimated time of arrival))
		System.out.println("Query4:");
		String sql = "SELECT    order.order_id , product.brand , product.name\r\n"
				+ "FROM      `411077008`.`delivery` , `411077008`.`order` , `411077008`.`product`\r\n"
				+ "WHERE     (delivery.eta <= (SELECT CURDATE())) and (delivery.statement != '已到貨') "
				+ "and (delivery.order_id = order.order_id) and (order.product_id = product.product_id)";
		try(ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("order id : " + rs.getInt("order_id"));
				System.out.println("brand : " + rs.getString("brand"));
				System.out.println("name : " + rs.getString("name"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	static void Query5(Statement st, Connection conn) {
		System.out.println("Query5:");
		System.out.println("資料更新前：");
		String sql = "select delivery.order_id, order.product_id, statement, shipment_date, eta, warehouse_inventory\n"
				+ "from `411077008`.`delivery`\n"
				+ "inner join `411077008`.`order`\n"
				+ "on order.order_id = delivery.order_id\n"
				+ "inner join `411077008`.`warehouse`\n"
				+ "on order.product_id = warehouse.product_id\n"
				+ "order by delivery.order_id\n"
				+ "\n";
		try(ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("order id : " + rs.getInt("delivery.order_id"));
				System.out.println("product id : " + rs.getInt("order.product_id"));
				System.out.println("statement : " + rs.getString("statement"));
				System.out.println("shipment date : " + rs.getDate("shipment_date"));
				System.out.println("eta : " + rs.getDate("eta"));
				System.out.println("warehouse inventory : " + rs.getInt("warehouse_inventory"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = "update `411077008`.`delivery`, `411077008`.`warehouse`\n"
				+ "set statement = '未出貨', shipment_date = now(), eta = date_add(now(), interval 14 day), warehouse_inventory = warehouse_inventory - 1\n"
				+ "where order_id = (select order_id\n"
				+ "from `411077008`.`delivery`\n"
				+ "where statement = '遺失/損壞') and product_id = (select product_id\n"
				+ "from `411077008`.`order`\n"
				+ "where order_id = (select order_id\n"
				+ "from `411077008`.`delivery`\n"
				+ "where statement = '遺失/損壞'));";
		try {
			PreparedStatement psUpdate = conn.prepareStatement(sql);
			psUpdate.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("資料更新後：");
		sql = "select delivery.order_id, order.product_id, statement, shipment_date, eta, warehouse_inventory\n"
				+ "from `411077008`.`delivery`\n"
				+ "inner join `411077008`.`order`\n"
				+ "on order.order_id = delivery.order_id\n"
				+ "inner join `411077008`.`warehouse`\n"
				+ "on order.product_id = warehouse.product_id\n"
				+ "order by delivery.order_id\n"
				+ "\n";
		try(ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("order id : " + rs.getInt("delivery.order_id"));
				System.out.println("product id : " + rs.getInt("order.product_id"));
				System.out.println("statement : " + rs.getString("statement"));
				System.out.println("shipment date : " + rs.getDate("shipment_date"));
				System.out.println("eta : " + rs.getDate("eta"));
				System.out.println("warehouse inventory : " + rs.getInt("warehouse_inventory"));
				System.out.println("=============================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
}
