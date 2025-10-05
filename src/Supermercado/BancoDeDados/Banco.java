package Supermercado.BancoDeDados;

public class Banco {

    /*CREATE DATABASE supermercado;
    USE supermercado;

    CREATE TABLE users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    is_admin BOOLEAN NOT NULL
);

    CREATE TABLE products (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

    CREATE TABLE purchases (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT,
            total DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
            );

    CREATE TABLE purchase_items (
            id INT AUTO_INCREMENT PRIMARY KEY,
            purchase_id INT,
            product_id INT,
            quantity INT,
            FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
            ); */
}


