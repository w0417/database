package database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

	public static void main(String[] args) {
		String url = "jdbc:mariadb://140.127.74.226:3306/411077008";
		String user = "411077008";
		String pwd = "411077008";
		ArrayList<Integer> price = new ArrayList<Integer>();
		price = initialize(price);
		Scanner scan = new Scanner(System.in);
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Connection conn = DriverManager.getConnection(url,user,pwd);
			//System.out.println("connect");
			Statement st = conn.createStatement();
			System.out.println("1.customer service --> check inventory at stores\n"
					+ "2.call center staff_1 --> lookup customer infromation\n"
					+ "3.call center staff_2 --> quickly enter orders\n"
					+ "4.warehouse staff_1 --> update the inventory of product in the warehouse\n"
					+ "5.warehouse staff_2 --> insert new product info in the warehouse and product table\n"
					+ "** enter 0 exit **");
			System.out.println("Please enter the command number you want: ");
			A: while (true) {
					String input = scan.nextLine();
					switch(input) {
					case "0":
						break A;
					case "1":
						//customer service
						customer_service(st);
						break;
					case "2":
						//call center staff_1(lookup customer info)
						call_center_staff1(st);
						break;
					case "3":
						//call center staff_2(quickly enter orders)
						System.out.println("Is he/she a new customer ? <yes or no> ");
						if(scan.nextLine().equals("yes")) {
							call_center_staff_nc(st,conn,scan);
							call_center_staff2(st,conn,price,scan);
						}
						else {
							call_center_staff2(st,conn,price,scan);
						}
						break;
					case "4":
						warehouse_staff1(st,conn,scan);
						break;
					case "5":
						warehouse_staff2(st,conn,scan);
						break;
					default:
						System.out.println("Please enter the right number again :");
						break;
					}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scan.close();
	}
	
	static ArrayList<Integer> initialize (ArrayList<Integer> price) {
		price.add(6900);
		price.add(28000);
		price.add(7000);
		price.add(30900);
		return price;
	}
	
	static  void customer_service (Statement st) {
		//查看附近商店的庫存(提供商店以及商品的資訊)
		String sql = "SELECT    store_name , telephone , address , store_inventory, brand , name\r\n"
				+ "FROM      `411077008`.store , `411077008`.product\r\n"
				+ "WHERE     store.product_id = product.product_id\r\n"
				+ "ORDER BY  store_name asc, product.product_id asc";
		try(ResultSet rs = st.executeQuery(sql)){
			int i = 0;
			while(rs.next()) {
				if((i%4 == 0)) {
					System.out.println("<store info>");
					System.out.println("store name : " + rs.getString("store_name"));
					System.out.println("telephone : " + rs.getString("telephone"));
					System.out.println("address : " + rs.getString("address"));
					
				}
				System.out.println("\tbrand : " + rs.getString("brand"));
				System.out.println("\tname : " + rs.getString("name"));
				System.out.println("\tinventory : " + rs.getString("store_inventory"));
				System.out.println("\t=============================");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void call_center_staff1(Statement st) {
		// 1.lookup customer info
		String sql = "SELECT    customer_id, name, phone, address\r\n"
				+ "FROM      `411077008`.customer";
		try(ResultSet rs = st.executeQuery(sql)){
			System.out.println("<customer info>");
			while(rs.next()) {
				System.out.println("\tcustomer id : " + rs.getString("customer_id"));
				System.out.println("\tname : " + rs.getString("name"));
				System.out.println("\tphone : " + rs.getString("phone"));
				System.out.println("\taddress : " + rs.getString("address"));
				System.out.println("\t=====================================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void call_center_staff2(Statement st, Connection conn, ArrayList<Integer> price, Scanner scan) {
		// 2.quickly enter orders
		PreparedStatement pst;
		String sql = "SELECT    count(*)\r\n"
				+ "FROM      `411077008`.`order`";
		int count = 0;
		try {
			//use placeholder represent the parameters until the user enter the order info
			pst = conn.prepareStatement("INSERT INTO `411077008`.`order`\r\n"
			+ "(order_id,product_id,order_date,product_number,total_amount,customer_id,contract_id) VALUES ( ?, ?, ?, ?, ?, ?, ? )");
			try(ResultSet rs = st.executeQuery(sql)){ //current number of orders
				while(rs.next()) {
					count = rs.getInt("count(*)")+1;
				}
			}
			System.out.println("<product id>: you can choose one of it.\n"
					+ "1:\n\tType: 音響\n\tBrand: Bose\n\tName: Sound Mini 揚聲器II\n\tPrice: 6900\n"
					+ "2:\n\tType: 手機\n\tBrand: Samsung\n\tName: Galazy S23\n\tPrice: 28000\n"
					+ "3:\n\tType: 螢幕\n\tBrand: ASUS\n\tName: TUF Gaming VG249QM1A\n\tPrice: 7000\n"
					+ "4:\n\tType: 電腦\n\tBrand: Apple\n\tName: MacBook Air\n\tPrice: 30900\n"
					+ "=============================");
			//user enter the information of order
			System.out.println("Please enter the following 5 input：\n"
					+ "<customer id> --> integer\n"
					+ "<product id> --> integer(according to the upper content)\n"
					+ "<contract id> --> integer or NULL(if it's not contract order then enter NULL)\n"
					+ "<order date> --> yyyy/mm/dd\n"
					+ "<number> --> integer(number of the product that customer bought)\n"
					+ "** Please seperate with space **\n"
					+ "=============================");
			String[] order_info = scan.nextLine().split(" ");
			//set the insert parameters
			pst.setInt(1, count+1);	//order_id
			pst.setInt(2, Integer.parseInt(order_info[1]));		//product_id
			pst.setString(3, order_info[3]);	//order_date
			pst.setInt(4, Integer.parseInt(order_info[4]));		//product_number
			pst.setInt(5, Integer.parseInt(order_info[4])*(price.get(Integer.parseInt(order_info[1])-1)));	//total_amount
			pst.setInt(6, Integer.parseInt(order_info[0]));		//customer_id
			String contract_id = order_info[2];
			if (Character.isDigit(contract_id.charAt(0))) {
				pst.setInt(7, Integer.parseInt(order_info[2]));	//contract_id
			}
			pst.setNull(7, Types.INTEGER);		//contract_id(if it is NULL)
			pst.executeQuery();
			pst.clearParameters();
			
			//show the insert result
			System.out.println("here is your insert order: ");
			pst = conn.prepareStatement("SELECT    order_id, product_id, order_date, product_number, total_amount, customer_id, contract_id\r\n"
					+ "FROM      `411077008`.`order`\r\n"
					+ "WHERE     order_id = ?");
			pst.setInt(1, count);
			
			try(ResultSet rs2 = pst.executeQuery()){
				while (rs2.next()) {
					System.out.println("order id : " + rs2.getInt("order_id"));
					System.out.println("customer id : " + rs2.getInt("customer_id"));
					System.out.println("product id : " + rs2.getInt("product_id"));
					System.out.println("order date : " + rs2.getString("order_date"));
					System.out.println("number : " + rs2.getInt("product_number"));
					System.out.println("total : " + rs2.getInt("total_amount"));
					System.out.println("contract id : " + rs2.getInt("contract_id"));
					System.out.println("=============================");
				}
			}
			pst.clearParameters();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void call_center_staff_nc(Statement st, Connection conn, Scanner scan) {
		//insert a new customer into table
		PreparedStatement pst;
		String sql_c = "SELECT    count(*)\r\n"
				+ "FROM      `411077008`.`customer`";
		String sql_oc = "SELECT    count(*)\r\n"
				+ "FROM      `411077008`.`online_customer`";
		int count_c = 0;
		int count_oc = 0;
		try {
			//use placeholder represent the parameters until the user enter the customer info
			pst = conn.prepareStatement("INSERT INTO `411077008`.`customer`\r\n"
			+ "(customer_id,name,phone,address) VALUES ( ?, ?, ?, ? )");
			
			try(ResultSet rs = st.executeQuery(sql_c)){ //current number of customers
				while(rs.next()) {
					count_c = rs.getInt("count(*)")+1;
				}
			}
			try(ResultSet rs = st.executeQuery(sql_oc)){ //current number of online customers
				while(rs.next()) {
					count_oc = rs.getInt("count(*)")+1;
				}
			}
			
			//user enter the information of customer
			System.out.println("Please enter the following 3 input：\n"
					+ "<customer name> --> string\n"
					+ "<product phone number> --> string\n"
					+ "<customer address> --> string\n"
					+ "** Please seperate with space **\n"
					+ "=============================");
			String[] customer_info = scan.nextLine().split(" ");
			//set the insert parameters
			pst.setInt(1, count_c);		//customer_id
			pst.setString(2, customer_info[0]);		//name
			pst.setString(3, customer_info[1]);		//phone
			pst.setString(4, customer_info[2]);		//address
			pst.executeQuery();
			pst.clearParameters();
			
			//show the new customer insert result
			System.out.println("here is your insert new customer: ");
			pst = conn.prepareStatement("SELECT    customer_id, name, phone, address\r\n"
					+ "FROM      `411077008`.`customer`\r\n"
					+ "WHERE     customer_id = ?");
			pst.setInt(1, count_c);
			
			try(ResultSet rs2 = pst.executeQuery()){
				while (rs2.next()) {
					System.out.println("customer id : " + rs2.getInt("customer_id"));
					System.out.println("customer name : " + rs2.getString("name"));
					System.out.println("customer phone number : " + rs2.getString("phone"));
					System.out.println("customer address : " + rs2.getString("address"));
					System.out.println("=============================");
				}
			}
			pst.clearParameters();
			
			//insert a new online customer
			pst = conn.prepareStatement("INSERT INTO `411077008`.`online_customer`\r\n"
			+ "(online_customer_id,customer_id) VALUES ( ?, ? )");
			pst.setInt(1, count_oc);		//online_customer_id
			pst.setInt(2, count_c);			//customer_id
			pst.executeQuery();
			pst.clearParameters();
			
			//show the new online customer insert result
			System.out.println("here is your insert new online customer: ");
			pst = conn.prepareStatement("SELECT    online_customer_id, customer_id\r\n"
					+ "FROM      `411077008`.`online_customer`\r\n"
					+ "WHERE     customer_id = ?");
			pst.setInt(1, count_c);
			
			try(ResultSet rs2 = pst.executeQuery()){
				while (rs2.next()) {
					System.out.println("online customer id : " + rs2.getInt("online_customer_id"));
					System.out.println("customer id : " + rs2.getInt("customer_id"));
					System.out.println("=============================\n");
				}
			}
			pst.clearParameters();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void warehouse_staff1(Statement st, Connection conn, Scanner scan) {
		//1.update the inventory info in the warehouse
		PreparedStatement pst;
		ArrayList<Integer> number = new ArrayList<Integer>();
		String sql = "SELECT    warehouse.product_id, warehouse_inventory, `type`, brand, name, price\r\n"
				+ "FROM      `411077008`.warehouse, `411077008`.product\r\n"
				+ "WHERE    warehouse.product_id = product.product_id";
		
		try {
			//show the product info in the warehouse now
			System.out.println("Products in the warehouse: ");
			try(ResultSet rs = st.executeQuery(sql)){
				while(rs.next()) {
					System.out.println("product id: " + rs.getInt("product_id"));
					System.out.println("inventory: " + rs.getInt("warehouse_inventory"));
					System.out.println("product type: " + rs.getString("type"));
					System.out.println("product brand: " + rs.getString("brand"));
					System.out.println("product name: " + rs.getString("name"));
					System.out.println("product price: " + rs.getInt("price"));
					System.out.println("=============================");
					number.add(rs.getInt("warehouse_inventory"));	//record the number of product in warehouse now
				}
			}
			pst = conn.prepareStatement("UPDATE `411077008`.warehouse SET warehouse_inventory = ? "
					+ "WHERE product_id = ?");
			System.out.println("Please enter the <product id> and <inventory> that you want to update\n"
					+ "** <inventory>: please enter the number of incoming shipments **\n"
					+ "** Please seperate with space **\n"
					+ "=============================");
			//user enter the info of the inventory they want to update
			String[] inventory_update = scan.nextLine().split(" ");
			//set the parameter of update
			pst.setInt(2, Integer.parseInt(inventory_update[0]));		//product_id
			pst.setInt(1, (Integer.parseInt(inventory_update[1]) + number.get(Integer.parseInt(inventory_update[0])-1)));		//warehouse_inventory
			//原本的數量加上新進來的貨物數量，進行庫存數量的更新
			pst.executeQuery();
			pst.clearParameters();
			
			//show the update result
			System.out.println("here is your update: ");
			pst = conn.prepareStatement("SELECT    warehouse.product_id, warehouse_inventory, `type`, brand, name, price\r\n"
				+ "FROM      `411077008`.warehouse, `411077008`.product\r\n"
				+ "WHERE    (warehouse.product_id = product.product_id) and (warehouse.product_id = ?)");
			pst.setInt(1, Integer.parseInt(inventory_update[0]));
			
			try(ResultSet rs2 = pst.executeQuery()){
				while (rs2.next()) {
					System.out.println("product id : " + rs2.getInt("product_id"));
					System.out.println("inventory : " + rs2.getInt("warehouse_inventory"));
					System.out.println("product type : " + rs2.getString("type"));
					System.out.println("product brand : " + rs2.getString("brand"));
					System.out.println("product name : " + rs2.getString("name"));
					System.out.println("product price : " + rs2.getInt("price"));
					System.out.println("=============================");
				}
			}
			pst.clearParameters();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void warehouse_staff2(Statement st, Connection conn, Scanner scan) {
		//insert the new product info in the warehouse
		PreparedStatement pst;
		String sql = "SELECT    count(product_id)\r\n"
				+ "FROM      `411077008`.product";
		int count = 0;
		
		try {
			
			try(ResultSet rs = st.executeQuery(sql)){
				while(rs.next()) {
					count = rs.getInt("count(product_id)")+1;
				}
			}
			
			pst = conn.prepareStatement("INSERT INTO `411077008`.product\r\n"
					+ "	( product_id, `type`, brand, name, price) VALUES ( ?, ?, ?, ?, ? )");
			System.out.println("Please enter the infromation of new incoming product: \n"
					+ "<product type> --> string\n"
					+ "<product brand> --> string\n"
					+ "<product price> --> integer\n"
					+ "** Please seperate with space **\n"
					+ "=============================");
			//user enter the info of the new product
			String[] product_info = scan.nextLine().split(" ");
			//because the name of product may have space in it
			System.out.println("Please enter the product name\n<product name> --> string");
			String product_name = scan.nextLine();
			
			//set the parameters of insert
			pst.setInt(1, count);		//produtct.product_id
			pst.setString(2, product_info[0]);		//type
			pst.setString(3, product_info[1]);		//brand
			pst.setString(4, product_name);			//name
			pst.setInt(5, Integer.parseInt(product_info[2]));		//price
			pst.executeQuery();
			pst.clearParameters();
			
			pst = conn.prepareStatement("INSERT INTO `411077008`.warehouse\r\n"
					+ "	( product_id, warehouse_inventory) VALUES ( ?, ? )");
			System.out.println("Please enter the number(inventory) of new product in warehouse: ");
			//user enter the number of the new product in the warehouse
			int inventory = Integer.parseInt(scan.nextLine());
			//set the parameters of insert
			pst.setInt(1, count);		//warehouse.product_id
			pst.setInt(2, inventory);	//warehouse_inventory
			pst.executeQuery();
			pst.clearParameters();
			
			//show the insert result
			pst = conn.prepareStatement("SELECT    warehouse.product_id, warehouse_inventory, `type`, brand, name, price\r\n"
					+ "FROM      `411077008`.warehouse, `411077008`.product\r\n"
					+ "WHERE    (warehouse.product_id = product.product_id) and (warehouse.product_id = ?)");
			pst.setInt(1, count);
			
			System.out.println("here is your insert: ");
			try(ResultSet rs2 = pst.executeQuery()){
				while (rs2.next()) {
					System.out.println("product id : " + rs2.getInt("product_id"));
					System.out.println("inventory : " + rs2.getInt("warehouse_inventory"));
					System.out.println("product type : " + rs2.getString("type"));
					System.out.println("product brand : " + rs2.getString("brand"));
					System.out.println("product name : " + rs2.getString("name"));
					System.out.println("product price : " + rs2.getInt("price"));
					System.out.println("=============================");
				}
			}
			pst.clearParameters();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
