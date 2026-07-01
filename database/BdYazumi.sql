-- ==========================================
-- BASE DE DATOS APP CLIENTES YAZUMI
-- MYSQL
-- ==========================================

CREATE DATABASE IF NOT EXISTS yazumi_app;
USE yazumi_app;

-- ==========================================
-- CODIGOS DE VALIDACION
-- ==========================================

CREATE TABLE codigos_validacion (
id_codigo INT AUTO_INCREMENT PRIMARY KEY,
codigo VARCHAR(50) NOT NULL UNIQUE,
usado TINYINT(1) DEFAULT 0,
fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- USUARIOS
-- ==========================================

CREATE TABLE usuarios (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nombres VARCHAR(100) NOT NULL,
telefono VARCHAR(15) NOT NULL UNIQUE,
direccion VARCHAR(255) NOT NULL,
nombre_negocio VARCHAR(150),
password_hash VARCHAR(255) NOT NULL,
estado TINYINT(1) DEFAULT 1,
fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,

id_codigo INT,

CONSTRAINT fk_usuario_codigo
FOREIGN KEY (id_codigo)
REFERENCES codigos_validacion(id_codigo)

);

-- ==========================================
-- PRODUCTOS
-- ==========================================

CREATE TABLE productos (
id_producto INT AUTO_INCREMENT PRIMARY KEY,
codigo_producto VARCHAR(50),
nombre VARCHAR(150) NOT NULL,
descripcion TEXT,
presentacion VARCHAR(100),
precio DECIMAL(10,2) NOT NULL,
stock INT DEFAULT 0,
imagen VARCHAR(255),
activo TINYINT(1) DEFAULT 1,
fecha_actualizacion DATETIME
);

-- ==========================================
-- CARRITO
-- UN SOLO CARRITO POR USUARIO
-- ==========================================

CREATE TABLE carrito (
id_carrito INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT NOT NULL UNIQUE,
fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_carrito_usuario
FOREIGN KEY (id_usuario)
REFERENCES usuarios(id_usuario)
ON DELETE CASCADE


);

-- ==========================================
-- DETALLE CARRITO
-- ==========================================

CREATE TABLE carrito_detalle (
id_detalle INT AUTO_INCREMENT PRIMARY KEY,
id_carrito INT NOT NULL,
id_producto INT NOT NULL,
cantidad INT NOT NULL,
precio_unitario DECIMAL(10,2) NOT NULL,

CONSTRAINT fk_detalle_carrito
FOREIGN KEY (id_carrito)
REFERENCES carrito(id_carrito)
ON DELETE CASCADE,

CONSTRAINT fk_detalle_producto
FOREIGN KEY (id_producto)
REFERENCES productos(id_producto),

CONSTRAINT uk_carrito_producto
UNIQUE(id_carrito, id_producto)

);

-- ==========================================
-- ESTADOS PEDIDO
-- ==========================================

CREATE TABLE estados_pedido (
id_estado INT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL
);

INSERT INTO estados_pedido VALUES
(1,'Registrado'),
(2,'En preparación'),
(3,'En camino'),
(4,'Entregado'),
(5,'Cancelado');

-- ==========================================
-- PEDIDOS
-- ==========================================

CREATE TABLE pedidos (
id_pedido INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT NOT NULL,
id_estado INT NOT NULL DEFAULT 1,
fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
direccion_entrega VARCHAR(255) NOT NULL,
total DECIMAL(10,2) NOT NULL DEFAULT 0,

CONSTRAINT fk_pedido_usuario
FOREIGN KEY (id_usuario)
REFERENCES usuarios(id_usuario),

CONSTRAINT fk_pedido_estado
FOREIGN KEY (id_estado)
REFERENCES estados_pedido(id_estado)

);

-- ==========================================
-- DETALLE PEDIDO
-- ==========================================

CREATE TABLE detalle_pedido (
id_detalle INT AUTO_INCREMENT PRIMARY KEY,
id_pedido INT NOT NULL,
id_producto INT NOT NULL,
cantidad INT NOT NULL,
precio_unitario DECIMAL(10,2) NOT NULL,
subtotal DECIMAL(10,2) NOT NULL,

CONSTRAINT fk_detallepedido_pedido
FOREIGN KEY (id_pedido)
REFERENCES pedidos(id_pedido)
ON DELETE CASCADE,

CONSTRAINT fk_detallepedido_producto
FOREIGN KEY (id_producto)
REFERENCES productos(id_producto),

CONSTRAINT uk_pedido_producto
UNIQUE(id_pedido, id_producto)
);

-- ==========================================
-- HISTORIAL ESTADOS
-- ==========================================

CREATE TABLE historial_estados (
id_historial INT AUTO_INCREMENT PRIMARY KEY,
id_pedido INT NOT NULL,
id_estado INT NOT NULL,
fecha DATETIME DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_historial_pedido
FOREIGN KEY (id_pedido)
REFERENCES pedidos(id_pedido)
ON DELETE CASCADE,

CONSTRAINT fk_historial_estado
FOREIGN KEY (id_estado)
REFERENCES estados_pedido(id_estado)

);

-- ==========================================
-- INDICES
-- ==========================================

CREATE INDEX idx_producto_nombre
ON productos(nombre);

CREATE INDEX idx_pedido_usuario
ON pedidos(id_usuario);

CREATE INDEX idx_historial_pedido
ON historial_estados(id_pedido);

-- ==========================================
-- DATOS DE PRUEBA
-- ==========================================

INSERT INTO codigos_validacion(codigo)
VALUES ('YAZUMI2026');

INSERT INTO usuarios (
nombres,
telefono,
direccion,
nombre_negocio,
password_hash,
id_codigo
)
VALUES (
'Cliente Demo',
'987654321',
'Av. Principal 123',
'Tienda Demo',
'$2a$10$EjemploHashBCrypt',
1
);

INSERT INTO productos (
codigo_producto,
nombre,
descripcion,
presentacion,
precio,
stock,
activo
)
VALUES
(
'LAYS001',
'Papas Lays Clásicas',
'Papas fritas clásicas',
'Bolsa 40g',
2.50,
100,
1
),
(
'DORI001',
'Doritos Queso',
'Snack sabor queso',
'Bolsa 45g',
3.00,
80,
1
),
(
'CHEE001',
'Cheetos',
'Snack sabor queso',
'Bolsa 35g',
2.20,
120,
1
);
