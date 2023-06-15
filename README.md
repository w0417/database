資料庫期末專案READ ME文件
411077001周雅婷/411077008楊昀恩
<執行說明>
[1]	校外請使用VPN連線，校內網則可以直接連上資料庫
[2]	沒有介面的實作，使用Java呈現結果(console)
[3]	程式碼分為Query.java、Application.java。要使用查詢語句請執行Query.java；若為不同使用者要使用資料庫的功能則執行Application.java
[4]	Query.java內包含期末專案所要求的5個查詢語句程式碼
[5]	Application.java包含5個使用者會需要用到的功能，請根據檔案中的指示進行選擇及輸入資料
選擇要使用的功能，分為5種：
1.	客戶服務 → 查看各商店的庫存
2.	呼叫中心 → 查看顧客的資訊
3.	呼叫中心 → 快速新增訂單
4.	倉庫 → 紀錄進貨
5.	倉庫 → 更新庫存
6.	輸入0結束程式
輸入1以查看商店的庫存：
	可以看到各個分店的存貨狀況，包含產品資訊及庫存數量
 
輸入3以新增訂單：
	若為新客戶，則輸入新客戶的資訊為其新增進資料庫，並可看到該筆新增的客戶資料，且會預設為線上客戶(因為是電話訂購視為線上訂購) 
	接著為新增訂單(或是原本即為舊客戶直接新增訂單也是這部分的步驟)
	可以從<product id>查看要新增的訂單產品為何者，於下方根據規定依序輸入訂單所需資訊：
<customer id>、<product id>、<contract id>、<order date>、<number>，並以空白鍵隔開
	最後可看到所新增的訂單資料(由資料庫撈出的資料)
 
輸入4以更新倉庫的庫存：
	會先顯示倉庫中目前所存的商品及數量，可作為下面輸入的參考
	輸入要更改的<product_id>、<inventory>，對新進的貨物做數量上的更改，並以空白鍵隔開
	最後可看到所更改的存貨資料(由資料庫撈出的資料)

 
< ER Diagram >
	訂單跟客戶之間存在『購買』關係，為一對多的關係(一位客戶可以有多筆訂單)，且訂單為total participation
	訂單跟商品存在『細節』關係，為一對多的關係(一筆訂單可以有多樣商品)，且訂單為total participation
	訂單跟貨運存在『追蹤』關係，為一對一的關係
	商品跟商店與倉庫各自存在『儲存』關係，為多對多以及一對多的關係(只有一個倉庫)
	線上客戶跟信用卡存在『擁有』關係，為一對多的關係(一位客戶可以有多張信用卡)，且信用卡為total participation
	合約客戶跟合約存在『擁有』關係，為一對多的關係(一位客戶可以有多筆合約)，且合約為total participation
	合約跟訂單存在『包含』關係，為一對一的關係
 
< Relational Schema >
	Customer：
	customer_id	(INT)
	name		(VARCHAR)
	phone		(VARCHAR)
	address		(VARCHAR)
	Online_customer：
	online_customer_id		(INT)
	customer_id			(INT)
	Normal_customer：
	normal_cusomer_id		(INT)
	customer_id			(INT)
	Contract_customer：
	contract_customer_id	(INT)
	customer_id			(INT)
	Contract：
	contract_id			(INT)
	cc_id				(INT)
	account_number		(VARCHAR)
	Card：
	card_id				(INT)
	oc_id				(INT)
	bank_name			(VARCHAR)
	card_number			(VARCHAR)
	good_thru			(DATE)
	Order：
	order_id				(INT)
	product_id			(INT)
	customer_id			(INT)
	contract_id			(INT)
	order_date			(DATE)
	product_number		(INT)
	total_amount			(INT)
	Product：
	product_id			(INT)
	type					(VARCHAR)
	brand				(VARCHAR)
	name				(VARCHAR)
	price					(INT)
	Delivery：
	delivery_id			(INT)
	oc_id				(INT)
	order_id				(INT)
	statement				(VARCHAR)
	shipment_date			(DATE)
	eta					(DATE)
	Warehouse：
	product_id			(INT)
	warehouse_inventory	(INT)
	Store：
	store_id				(INT)
	product_id			(INT)
	store_name			(VARCHAR)
	telephone				(VARCHAR)
	address				(VARCHAR)
	store_inventory		(INT)
 
< Table資料量：count(*) >
 

 
< 查詢結果截圖 >
