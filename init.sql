-- Tạo database webjava
DROP DATABASE IF EXISTS web_java;
CREATE DATABASE IF NOT EXISTS web_java;
USE web_java;

-- Tạo bảng users
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       phone VARCHAR(20) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role ENUM('admin', 'customer') NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

-- Tạo bảng reviews
CREATE TABLE reviews (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         customer_id INT NOT NULL,
                         rating INT NOT NULL,
                         content TEXT NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES users(id)
);

-- Tạo bảng reservations
CREATE TABLE reservations (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              total_people INT NOT NULL,
                              status ENUM('pending', 'confirmed', 'cancelled') NOT NULL,
                              reservation_at TIMESTAMP NOT NULL,
                              note TEXT,
                              total_price FLOAT NOT NULL,
                              created_at TIMESTAMP NOT NULL,
                              customer_id INT NOT NULL,
                              FOREIGN KEY (customer_id) REFERENCES users(id)
);

ALTER TABLE reservations
    ADD COLUMN table_id INT,
ADD CONSTRAINT fk_reservation_table 
FOREIGN KEY (table_id) REFERENCES tables(id);

-- Tạo bảng tables
CREATE TABLE tables (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        capacity INT NOT NULL,
    -- available: trống
    -- occupied: đang có khách
    -- reserved: đã đặt trước
    -- maintenance: đang bảo trì
                        status ENUM('available', 'occupied', 'reserved', 'maintenance') NOT NULL,
                        location TEXT NOT NULL
);

-- Tạo bảng reservation_table (bảng trung gian)
CREATE TABLE reservation_table (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   reservation_id INT NOT NULL,
                                   table_id INT NOT NULL,
                                   FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                                   FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- Tạo bảng combo
CREATE TABLE combos (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        price FLOAT NOT NULL,
                        description TEXT NOT NULL,
                        status ENUM('available', 'unavailable') NOT NULL,
                        image_url VARCHAR(100) NOT NULL
);

-- Tạo bảng food
CREATE TABLE foods (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       description TEXT NOT NULL,
                       price FLOAT NOT NULL,
                       image_url VARCHAR(100) NOT NULL,
                       status ENUM('available', 'unavailable') NOT NULL,
                       meal_type ENUM('breakfast', 'lunch', 'dinner', 'dessert')
);
ALTER TABLE foods
    MODIFY COLUMN image_url VARCHAR(255) NOT NULL;


-- Tạo bảng combo_food (bảng trung gian)
CREATE TABLE combo_food (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            combo_id INT NOT NULL,
                            food_id INT NOT NULL,
                            quantity INT NOT NULL,
                            FOREIGN KEY (combo_id) REFERENCES combos(id) ON DELETE CASCADE,
                            FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
);

-- Tạo bảng reservation_food (bảng trung gian)
CREATE TABLE reservation_food (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  reservation_id INT NOT NULL,
                                  food_id INT NOT NULL,
                                  quantity INT NOT NULL,
                                  FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                                  FOREIGN KEY (food_id) REFERENCES foods(id)
);

-- Tạo bảng reservation_combo (bảng trung gian)
CREATE TABLE reservation_combo (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   reservation_id INT NOT NULL,
                                   combo_id INT NOT NULL,
                                   quantity INT NOT NULL,
                                   FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                                   FOREIGN KEY (combo_id) REFERENCES combos(id)
);
-- Tạo index giúp query nhanh hơn

-- Index cho bảng users (email, phone)
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);

-- Index cho bảng reviews (rating)
CREATE INDEX idx_reviews_rating ON reviews(rating);

-- Index cho bảng reservations (status, reservation_at, customer_id)
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_reservations_reservation_at ON reservations(reservation_at);
CREATE INDEX idx_reservations_customer_id ON reservations(customer_id);

-- Index cho bảng tables (status)
CREATE INDEX idx_tables_status ON tables(status);

-- Index cho bảng combo (status)
CREATE INDEX idx_combos_status ON combos(status);

-- Index cho bảng food (status)
CREATE INDEX idx_foods_status ON foods(status);


-- Chèn dữ liệu mẫu vào bảng users
-- password: 12345678
INSERT INTO users (name, email, phone, password, role, created_at) VALUES
                                                                       ('Admin User', 'admin@gmail.com', '0901234567', '$2a$10$Hd2iSSXPv5GNck5IxLtQN.ykL74PRyFrAun9H/DPqU28qcrkj1B4y', 'admin', NOW()),
                                                                       ('Staff User', 'staff@gmail.com', '0909876543', '$2a$10$Hd2iSSXPv5GNck5IxLtQN.ykL74PRyFrAun9H/DPqU28qcrkj1B4y', 'admin', NOW()),
                                                                       ('Customer 1', 'customer1@gmail.com', '0912345678', '$2a$10$Hd2iSSXPv5GNck5IxLtQN.ykL74PRyFrAun9H/DPqU28qcrkj1B4y', 'customer', NOW()),
                                                                       ('Customer 2', 'customer2@gmail.com', '0923456789', '$2a$10$Hd2iSSXPv5GNck5IxLtQN.ykL74PRyFrAun9H/DPqU28qcrkj1B4y', 'customer', NOW()),
                                                                       ('Customer 3', 'customer3@gmail.com', '0934567890', '$2a$10$Hd2iSSXPv5GNck5IxLtQN.ykL74PRyFrAun9H/DPqU28qcrkj1B4y', 'customer', NOW());

-- Chèn dữ liệu mẫu vào bảng tables
INSERT INTO tables (name, capacity, status, location) VALUES
                                                          ('Bàn A1', 2, 'available', 'Tầng 1 - Cửa sổ'),
                                                          ('Bàn A2', 2, 'occupied', 'Tầng 1 - Cửa sổ'),
                                                          ('Bàn B1', 4, 'available', 'Tầng 1 - Giữa'),
                                                          ('Bàn B2', 4, 'reserved', 'Tầng 1 - Giữa'),
                                                          ('Bàn C1', 6, 'available', 'Tầng 2 - Cửa sổ'),
                                                          ('Bàn C2', 8, 'reserved', 'Tầng 2 - Ban công'),
                                                          ('Bàn VIP1', 10, 'available', 'Tầng 3 - Phòng riêng');
INSERT INTO tables (name, capacity, status, location) VALUES
                                                          ('Bàn VIP7', 10, 'available', 'Tầng 3 - Phòng riêng'),
                                                          ('Bàn VIP8', 10, 'available', 'Tầng 3 - Phòng riêng'),
                                                          ('Bàn VIP9', 10, 'available', 'Tầng 3 - Phòng riêng'),
                                                          ('Bàn VIP10', 10, 'available', 'Tầng 3 - Phòng riêng');


-- Chèn dữ liệu mẫu vào bảng food
-- BREAKFAST
INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES
                                                                               ('Bánh mì ốp la', 'Bánh mì giòn với trứng chiên, ăn kèm dưa leo và tương ớt', 1.5, 'https://png.pngtree.com/png-vector/20240425/ourmid/pngtree-vietnamese-bread-egg-delicious-cartoon-png-image_12283193.png', 'available', 'breakfast'),
                                                                               ('Phở bò', 'Phở bò truyền thống với nước dùng đậm đà và thịt bò tái', 2.5, 'https://media.istockphoto.com/id/1299419373/vi/anh/pho-bo-vietnamese-soup.jpg?s=612x612&w=0&k=20&c=MTp9dmYOEV33fM8QD2XV0uHCerBdyZpCsm59TyLHSSc=', 'available', 'breakfast'),
                                                                               ('Xôi gà', 'Xôi nếp dẻo ăn cùng thịt gà, hành phi và nước tương', 2.0, 'https://static.vinwonders.com/production/ga_bo_xoi_ha_noi_3.jpg', 'available', 'breakfast'),
                                                                               ('Bún riêu', 'Bún với riêu cua, huyết và đậu hũ, nước dùng thơm ngon', 2.2, 'https://cdn.xanhsm.com/2025/01/7f24de71-bun-rieu-quy-nhon-1.jpg', 'available', 'breakfast'),
                                                                               ('Hủ tiếu', 'Hủ tiếu nước với thịt heo, tôm và hành lá', 2.3, 'https://i.vietgiaitri.com/2022/1/20/me-man-huong-vi-cua-loat-cac-phien-ban-hu-tieu-mien-nam-7de-6278394.jpg', 'available', 'breakfast');

-- LUNCH
INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES
                                                                               ('Cơm tấm sườn bì chả', 'Cơm tấm với sườn nướng, bì, chả trứng và nước mắm chua ngọt', 3.0, 'https://baolamdong.vn/file/e7837c02845ffd04018473e6df282e92/052023/1.com-tam-viet-nam-hap-dan-du-khach-khi-den-da-lat-2_20230529114050.jpg', 'available', 'lunch'),
                                                                               ('Bún thịt nướng', 'Bún với thịt heo nướng, rau sống và nước mắm chua ngọt', 2.8, 'https://cdn.tgdd.vn/2021/12/CookRecipe/GalleryStep/3-3.jpg', 'available', 'lunch'),
                                                                               ('Cơm gà xối mỡ', 'Cơm chiên giòn với gà chiên và nước mắm gừng', 3.2, 'https://dailong.asia/page/download/com-ga-xoi-mo-ngon-png-flushing-fat-chicken-rice-4GeLQy.png', 'available', 'lunch'),
                                                                               ('Bánh canh cua', 'Sợi bánh canh dai ngon với nước dùng từ cua tươi', 3.5, 'https://dienmaythiennamhoa.vn/static/images/Hinh%20Bai%20Ve%20Tinh/VAO%20BEP/3(14).PNG', 'available', 'lunch'),
                                                                               ('Mì Quảng', 'Mì Quảng với thịt gà, tôm, bánh tráng và đậu phộng', 2.9, 'https://images2.thanhnien.vn/528068263637045248/2024/11/12/1000022316-1731386167793124667085.jpg', 'available', 'lunch');

-- DINNER
INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES
                                                                               ('Lẩu Thái', 'Lẩu chua cay kiểu Thái với tôm, mực và rau', 5.5, 'https://cookbeo.com/media/2020/09/lau-thai-thap-cam/lau-thai-thap-cam.jpg', 'available', 'dinner'),
                                                                               ('Cháo sườn', 'Cháo gạo tẻ nấu mềm với sườn non', 2.5, 'https://hawonkoo.vn/medias/thumbs/74/images-2023-10-cach-nau-chao-suon-ngon-chuan-vi-ha-noi-don-gian-tai-nha-1-1200x0.jpg.webp', 'available', 'dinner'),
                                                                               ('Mì xào hải sản', 'Mì trứng xào với tôm, mực, và rau củ', 3.8, 'https://i-giadinh.vnecdn.net/2023/03/22/Thanh-pham-1-3557-1679473358.jpg', 'available', 'dinner'),
                                                                               ('Gỏi cuốn', 'Cuốn tôm thịt với rau sống và chấm mắm nêm', 2.0, 'https://khoailangthang.com/uploads/2023/goi-cuon-nguyen-cong-tru.jpg', 'available', 'dinner'),
                                                                               ('Bánh xèo', 'Bánh xèo giòn rụm với tôm, thịt và giá đỗ', 3.0, 'https://vietfood.org.vn/wp-content/uploads/2021/05/BanhXeo1.png', 'available', 'dinner');

-- DESSERT
INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES
                                                                               ('Chè ba màu', 'Chè với đậu đỏ, đậu xanh, nước cốt dừa và thạch rau câu', 1.2, 'https://takestwoeggs.com/wp-content/uploads/2021/09/Che%CC%80-Ba-Ma%CC%80u-Vietnamese-three-colored-dessert-Takestwoeggs-Final-SQ-500x375.jpg', 'available', 'dessert'),
                                                                               ('Bánh flan', 'Bánh flan mềm mịn ăn kèm cà phê và đá bào', 1.0, 'https://file.hstatic.net/1000396324/file/banh-flan-socola-2_71a4b9be8ddc459c9be9a7226ae74476.jpg', 'available', 'dessert'),
                                                                               ('Kem dừa', 'Kem dừa truyền thống trong trái dừa nhỏ, kèm đậu phộng', 1.8, 'https://file.hstatic.net/200000721249/file/cach_lam_kem_dua_matcha_92c03c90fe6c4e22806e1126feedc319.jpg', 'available', 'dessert'),
                                                                               ('Sữa chua nếp cẩm', 'Sữa chua ăn kèm nếp cẩm dẻo thơm, mát lạnh', 1.5, 'https://www.bartender.edu.vn/wp-content/uploads/2021/03/sua-chua-nep-cam-hap-dan.jpg', 'available', 'dessert'),
                                                                               ('Rau câu dừa', 'Rau câu hai lớp từ nước cốt dừa và nước dừa tươi', 1.3, 'https://lypham.vn/wp-content/uploads/2024/09/meo-lam-rau-cau-dua-soi.jpg', 'available', 'dessert');

-- Chèn dữ liệu mẫu vào bảng combo
SET SQL_SAFE_UPDATES = 1;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO combos (name, price, description, status, image_url) VALUES
                                                                     ('Combo cặp đôi', 250000, 'Combo dành cho 2 người gồm 2 món chính, 2 món tráng miệng và 2 đồ uống', 'available', 'https://picsum.photos/seed/combo1/200'),
                                                                     ('Combo gia đình', 450000, 'Combo dành cho 4 người gồm 4 món chính, 4 món tráng miệng và 4 đồ uống', 'available', 'https://picsum.photos/seed/combo2/200'),
                                                                     ('Combo tiệc nhỏ', 850000, 'Combo dành cho 8 người gồm 8 món chính, 8 món tráng miệng và 8 đồ uống', 'available', 'https://picsum.photos/seed/combo3/200');
DELETE FROM combos;
-- Chèn dữ liệu mẫu vào bảng combo
INSERT INTO combos (name, price, description, status, image_url) VALUES
                                                                     ('Combo cặp đôi', 250000, 'Combo dành cho 2 người gồm 2 món chính, 2 món tráng miệng và 2 đồ uống', 'available', 'https://goldenmeat.vn/wp-content/uploads/2023/09/3combo-bbq-990k167899_n.jpg'),
                                                                     ('Combo gia đình', 450000, 'Combo dành cho 4 người gồm 4 món chính, 4 món tráng miệng và 4 đồ uống', 'available', 'https://vuacalongphuong.com/static/users/vcce1b55489f1/products/1n-6236addcd6586.jpg'),
                                                                     ('Combo tiệc nhỏ', 850000, 'Combo dành cho 8 người gồm 8 món chính, 8 món tráng miệng và 8 đồ uống', 'available', 'https://thaiexpress.vn/wp-content/uploads/TE_Combo-adapt_WEB_1200x1200px.png');
-- Chèn dữ liệu mẫu vào bảng combo_food
INSERT INTO combo_food (combo_id, food_id, quantity) VALUES
                                                         (1, 6, 2), -- 2 Mì Ý cho combo cặp đôi
                                                         (1, 4, 2), -- 2 Bánh flan cho combo cặp đôi
                                                         (1, 2, 2), -- 2 Cà phê sữa cho combo cặp đôi
                                                         (2, 8, 2), -- 2 Bò bít tết cho combo gia đình
                                                         (2, 9, 2), -- 2 Pizza cho combo gia đình
                                                         (2, 4, 4), -- 4 Bánh flan cho combo gia đình
                                                         (2, 10, 4), -- 4 Nước cam cho combo gia đình
                                                         (3, 7, 2), -- 2 Gà nướng cho combo tiệc nhỏ
                                                         (3, 8, 3), -- 3 Bò bít tết cho combo tiệc nhỏ
                                                         (3, 9, 3), -- 3 Pizza cho combo tiệc nhỏ
                                                         (3, 5, 4), -- 4 Salad cho combo tiệc nhỏ
                                                         (3, 10, 8); -- 8 Nước cam cho combo tiệc nhỏ

-- Chèn dữ liệu mẫu vào bảng reservations
INSERT INTO reservations (total_people, status, reservation_at, note, total_price, created_at, customer_id) VALUES
                                                                                                                (2, 'confirmed', DATE_ADD(NOW(), INTERVAL 1 DAY), 'Kỷ niệm 2 năm yêu nhau', 350000, NOW(), 3),
                                                                                                                (4, 'confirmed', DATE_ADD(NOW(), INTERVAL 2 DAY), 'Sinh nhật con trai', 650000, NOW(), 4),
                                                                                                                (8, 'pending', DATE_ADD(NOW(), INTERVAL 5 DAY), 'Họp lớp', 1200000, NOW(), 5);

-- Chèn dữ liệu mẫu vào bảng reservation_table
INSERT INTO reservation_table (reservation_id, table_id) VALUES
                                                             (1, 2), -- Đặt bàn A2 cho reservation 1
                                                             (2, 4), -- Đặt bàn B2 cho reservation 2
                                                             (3, 6); -- Đặt bàn C2 cho reservation 3

-- Chèn dữ liệu mẫu vào bảng reservation_food
INSERT INTO reservation_food (reservation_id, food_id, quantity) VALUES
                                                                     (1, 2, 2), -- 2 Cà phê sữa cho reservation 1
                                                                     (1, 4, 2), -- 2 Bánh flan cho reservation 1
                                                                     (1, 6, 2), -- 2 Mì Ý cho reservation 1
                                                                     (2, 5, 1), -- 1 Salad cho reservation 2
                                                                     (2, 8, 2), -- 2 Bò bít tết cho reservation 2
                                                                     (2, 9, 1), -- 1 Pizza cho reservation 2
                                                                     (2, 10, 4); -- 4 Nước cam cho reservation 2

-- Chèn dữ liệu mẫu vào bảng reservation_combo
INSERT INTO reservation_combo (reservation_id, combo_id, quantity) VALUES
    (3, 3, 1); -- 1 Combo tiệc nhỏ cho reservation 3
select* from users;
select*from reservations;
SET SQL_SAFE_UPDATES = 1;
SET FOREIGN_KEY_CHECKS = 1;
DELETE FROM foods;

select*from reservation_food;
select*from reservation_combo;
ALTER TABLE reservations ;
select*from reviews;
-- Chèn dữ liệu mẫu vào bảng reviews
INSERT INTO reviews (customer_id, rating, content, created_at) VALUES
                                                                   (3, 5, 'Món ăn ngon, phục vụ nhiệt tình, không gian đẹp!', DATE_SUB(NOW(), INTERVAL 2 DAY)),
                                                                   (4, 4, 'Thức ăn ngon, giá cả hợp lý, tuy nhiên phục vụ hơi chậm vào giờ cao điểm.', DATE_SUB(NOW(), INTERVAL 1 DAY));