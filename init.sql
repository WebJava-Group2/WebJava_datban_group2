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

-- Chèn dữ liệu mẫu vào bảng food
INSERT INTO foods (name, description, price, image_url, status, meal_type) VALUES
('Cà phê đen', 'Cà phê đen nguyên chất, vị đắng đậm đà', 29000, 'https://picsum.photos/seed/food1/200', 'available', 'breakfast'),
('Cà phê sữa', 'Cà phê sữa béo ngậy', 35000, 'https://picsum.photos/seed/food2/200', 'available', 'breakfast'),
('Trà sen vàng', 'Trà hương sen thanh mát', 45000, 'https://picsum.photos/seed/food3/200', 'available', 'lunch'),
('Bánh flan', 'Bánh flan mềm mịn với caramel', 25000, 'https://picsum.photos/seed/food4/200', 'unavailable', 'dessert'),
('Salad trộn', 'Salad rau củ tươi ngon với sốt đặc biệt', 65000, 'https://picsum.photos/seed/food5/200', 'available', 'lunch'),
('Mì Ý sốt bò bằm', 'Mì Ý với sốt bò bằm đậm đà', 85000, 'https://picsum.photos/seed/food6/200', 'available', 'lunch'),
('Gà nướng', 'Gà nướng nguyên con với sốt BBQ', 250000, 'https://picsum.photos/seed/food7/200', 'available', 'dinner'),
('Bò bít tết', 'Bò bít tết Úc kèm khoai tây chiên', 180000, 'https://picsum.photos/seed/food7/200', 'unavailable', 'dinner'),
('Bánh pizza hải sản', 'Pizza hải sản phô mai với đế giòn', 150000, 'https://picsum.photos/seed/food8/200', 'available', 'dinner'),
('Nước cam tươi', 'Nước cam ép tươi 100%', 35000, 'https://picsum.photos/seed/food9/200', 'available', 'breakfast');
-- Chèn dữ liệu mẫu vào bảng combo
INSERT INTO combos (name, price, description, status, image_url) VALUES
('Combo cặp đôi', 250000, 'Combo dành cho 2 người gồm 2 món chính, 2 món tráng miệng và 2 đồ uống', 'available', 'https://picsum.photos/seed/combo1/200'),
('Combo gia đình', 450000, 'Combo dành cho 4 người gồm 4 món chính, 4 món tráng miệng và 4 đồ uống', 'available', 'https://picsum.photos/seed/combo2/200'),
('Combo tiệc nhỏ', 850000, 'Combo dành cho 8 người gồm 8 món chính, 8 món tráng miệng và 8 đồ uống', 'available', 'https://picsum.photos/seed/combo3/200');

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
select*from reservation_food;
ALTER TABLE reservations ;

-- Chèn dữ liệu mẫu vào bảng reviews
INSERT INTO reviews (customer_id, rating, content, created_at) VALUES
(3, 5, 'Món ăn ngon, phục vụ nhiệt tình, không gian đẹp!', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 4, 'Thức ăn ngon, giá cả hợp lý, tuy nhiên phục vụ hơi chậm vào giờ cao điểm.', DATE_SUB(NOW(), INTERVAL 1 DAY)); 