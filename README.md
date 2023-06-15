資料庫期末專案 READ ME 文件 

411077001 周雅婷/411077008 楊昀恩 

- 執行說明> 
1. 校外請使用 VPN 連線，校內網則可以直接連上資料庫
1. 沒有介面的實作，使用 Java 呈現結果(console) 
1. 程式碼分為 Query.java、Application.java。要使用查詢語句請執行 Query.java；若為不同使用者 要使用資料庫的功能則執行 Application.java 
1. Query.java 內包含期末專案所要求的 5 個查詢語句程式碼
1. Application.java 包含 5 個使用者會需要用到的功能，請根據檔案中的指示進行選擇及輸入資料 選擇要使用的功能，分為 **5** 種：** 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.001.png)

1. 客戶服務  → 查看各商店的庫存
1. 呼叫中心  →  查看顧客的資訊
1. 呼叫中心  →  快速新增訂單
1. 倉庫  →  紀錄進貨 
1. 倉庫  →  更新庫存 
1. 輸入 0 結束程式 

輸入 **1** 以查看商店的庫存：** 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.002.jpeg) ![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.003.jpeg)

- 可以看到各個分店的存貨狀況，包含產品資訊及庫存數量

輸入 **3** 以新增訂單：** 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.004.png)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.005.png)

- 若為新客戶，則輸入新客戶的資訊為其新增進資料庫，並可看到該筆新增的客戶資料，且會預 設為線上客戶(因為是電話訂購視為線上訂購)  

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.006.png)

- 接著為新增訂單(或是原本即為舊客戶直接新增訂單也是這部分的步驟) 
- 可以從<product id>查看要新增的訂單產品為何者，於下方根據規定依序輸入訂單所需資訊： <customer id>、<product id>、<contract id>、<order date>、<number>，並以空白鍵隔開

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.007.png)

- 最後可看到所新增的訂單資料(由資料庫撈出的資料) 

輸入 **4** 以更新倉庫的庫存：

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.008.png)

- 會先顯示倉庫中目前所存的商品及數量，可作為下面輸入的參考
- 輸入要更改的<product\_id>、<inventory>，對新進的貨物做數量上的更改，並以空白鍵隔開

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.009.png)

- 最後可看到所更改的存貨資料(由資料庫撈出的資料) 
- ER Diagram > 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.010.jpeg)

- 訂單跟客戶之間存在『購買』關係，為一對多的關係(一位客戶可以有多筆訂單)，且訂單 為 total participation 
- 訂單跟商品存在『細節』關係，為一對多的關係(一筆訂單可以有多樣商品)，且訂單為 total participation 
- 訂單跟貨運存在『追蹤』關係，為一對一的關係
- 商品跟商店與倉庫各自存在『儲存』關係，為多對多以及一對多的關係(只有一個倉庫) 
- 線上客戶跟信用卡存在『擁有』關係，為一對多的關係(一位客戶可以有多張信用卡)，且 信用卡為 total participation 
- 合約客戶跟合約存在『擁有』關係，為一對多的關係(一位客戶可以有多筆合約)，且合約 為 total participation 
- 合約跟訂單存在『包含』關係，為一對一的關係
- Relational Schema > 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.011.jpeg)

- Customer： 
  - customer\_id  (INT) 
  - name  (VARCHAR) 
  - phone  (VARCHAR) 
  - address   (VARCHAR) 
- Online\_customer： 
  - online\_customer\_id  (INT) 
  - customer\_id   (INT) 
- Normal\_customer： 
  - normal\_cusomer\_id  (INT) 
  - customer\_id   (INT) 
- Contract\_customer： 
  - contract\_customer\_id  (INT) 
  - customer\_id   (INT) 
- Contract： 
  - contract\_id  (INT) 
  - cc\_id  (INT) 
  - account\_number  (VARCHAR) 
- Card： 
- card\_id   (INT) 
- oc\_id  (INT) 
- bank\_name  (VARCHAR) 
- card\_number   (VARCHAR) 
- good\_thru  (DATE) 
- Order： 
  - order\_id   (INT) 
  - product\_id  (INT) 
  - customer\_id   (INT) 
  - contract\_id  (INT) 
  - order\_date  (DATE) 
  - product\_number  (INT) 
  - total\_amount   (INT) 
- Product： 
  - product\_id  (INT) 
  - type   (VARCHAR) 
  - brand  (VARCHAR) 
  - name  (VARCHAR) 
  - price  (INT) 
- Delivery： 
  - delivery\_id  (INT) 
  - oc\_id  (INT) 
  - order\_id   (INT) 
  - statement   (VARCHAR) 
  - shipment\_date   (DATE) 
  - eta   (DATE) 
- Warehouse： 
  - product\_id  (INT) 
  - warehouse\_inventory  (INT) 
- Store： 
- store\_id   (INT) 
- product\_id  (INT) 
- store\_name  (VARCHAR) 
- telephone   (VARCHAR) 
- address   (VARCHAR) 
- store\_inventory  (INT) 
- Table 資料量：count(\*) > 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.012.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.013.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.014.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.015.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.016.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.017.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.018.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.019.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.020.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.021.jpeg)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.022.jpeg)

- 查詢結果截圖  > 

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.023.png) ![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.024.png)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.025.png) ![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.026.png)

![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.027.jpeg) ![](Aspose.Words.09ef4581-1307-47e4-acf8-1dc8408ebb8d.028.jpeg)
