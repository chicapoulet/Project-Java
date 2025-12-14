CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- 1. Table Users
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL
);

-- 2. Table Categories
DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- 3. Table Products
DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- User exemples
INSERT INTO users (username, password, role) VALUES 
('admin', '$2a$10$Iq.J8.k/j.k.k/k.k/k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.e', 'ADMIN'),
('vendeur', '$2a$10$Iq.J8.k/j.k.k/k.k/k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.k.e', 'USER');

-- Catégories exemples
INSERT INTO categories (name) VALUES ('Électronique'), ('Vêtements'), ('Maison');

-- Produits exemples
INSERT INTO products (name, description, price, stock, category_id) VALUES 
('Ordinateur Portable', 'Dell XPS 13', 1200.00, 10, 1),
('T-Shirt Blanc', 'Coton Bio', 19.99, 50, 2),
('Lampe de Bureau', 'LED réglable', 35.50, 25, 3);
