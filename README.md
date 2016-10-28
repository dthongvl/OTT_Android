# OTT_Android
Các giá trị hiển thị trên màn hình như TextView, Button's text,...PHẢI khai báo trong strings.xml và gọi từ đó ra.
Các giá trị tên API, tên Event phải được khai báo trong class Commands.java. Khi sử dụng thì gọi trong đó ra.
Các Activity phải được kế thừa Interface IActivity, overide 2 phương thức là mapViewIDs và addEventListeners, thực hiện ánh xạ các control trong mapViewIDs, thực hiện lắng nghe sự kiện trong addEventListers, gọi 2 phương thức này ở onCreate.
Ở MainActivity mình có demo các công việc trên và demo connect tới server, gọi API, lắng nghe response.
