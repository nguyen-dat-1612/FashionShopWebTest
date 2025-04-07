Feature: Mua ngay thành công

  Scenario: Người dùng chọn sản phẩm và mua ngay
    Given Người dùng đã đăng nhập vào hệ thống
    And Sản phẩm có màu sắc và kích thước hợp lệ trên trang chi tiết sản phẩm
    When Người dùng nhấn "Mua ngay" trên trang chi tiết sản phẩm
    Then Sản phẩm được thêm vào giỏ hàng
    And Người dùng được chuyển hướng đến trang giỏ hàng với sản phẩm đã chọn