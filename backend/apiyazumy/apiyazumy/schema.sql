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
es_admin TINYINT(1) DEFAULT 0,
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
marca VARCHAR(100),
nombre VARCHAR(150) NOT NULL,
descripcion TEXT,
presentacion VARCHAR(100),
precio DECIMAL(10,2) NOT NULL,
stock INT DEFAULT 0,
imagen VARCHAR(255),
activo TINYINT(1) DEFAULT 1,
unidades_por_paquete INT DEFAULT 12,
precio_sugerido DECIMAL(10,2) DEFAULT 1.50,
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
-- INDICES
-- ==========================================

CREATE INDEX idx_producto_nombre
ON productos(nombre);

CREATE INDEX idx_pedido_usuario
ON pedidos(id_usuario);

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
'$2a$10$Li13F7GfeynCCEqCJH.1eOG1HGX3MwZAqX6zklWzS9zBosYPuZFyu',
1
);

INSERT INTO usuarios (
nombres,
telefono,
direccion,
nombre_negocio,
password_hash,
es_admin,
id_codigo
)
VALUES (
'Administrador',
'999999999',
'Oficina Principal',
'Yazumi Admin',
'$2a$10$j3Yp0/f4UjH3.21B8R/9feG8o6N5WJ.c7Xf4B9JjB1i7l1K9Lp8Wy',
1,
NULL
);

INSERT INTO productos (
    codigo_producto,
    marca,
    nombre,
    descripcion,
    presentacion,
    precio,
    stock,
    imagen,
    unidades_por_paquete,
    precio_sugerido,
    activo
)
VALUES
(
    'LAYS001',
    'Lays',
    'Cinta Lays Clásicas',
    'Cinta conteniendo 12 unidades de Papas Fritas Clásicas Lay''s de 45g. Formato listo para colgar y vender.',
    'Cinta de 12 un',
    14.40,
    50,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Lays_packet.jpg/320px-Lays_packet.jpg',
    12,
    1.50,
    1
),
(
    'LAYS002',
    'Lays',
    'Cinta Lays Ondas Limón',
    'Cinta conteniendo 12 unidades de Papas Fritas Lay''s Ondas sabor Limón de 45g. Ideal para exhibición en colgantes.',
    'Cinta de 12 un',
    14.40,
    45,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Lays_Sour_Cream_%26_Onion.jpg/320px-Lays_Sour_Cream_%26_Onion.jpg',
    12,
    1.50,
    1
),
(
    'LAYS003',
    'Lays',
    'Cinta Lays BBQ',
    'Cinta conteniendo 12 unidades de Papas Fritas Lay''s sabor Barbacoa (BBQ) de 45g. Sabor ahumado intenso.',
    'Cinta de 12 un',
    14.40,
    40,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Potato_chips_with_barbecue_flavor.jpg/320px-Potato_chips_with_barbecue_flavor.jpg',
    12,
    1.60,
    1
),
(
    'LAYS004',
    'Lays',
    'Cinta Lays Spicy',
    'Cinta conteniendo 12 unidades de Papas Fritas Lay''s sabor Picante de 45g. Nivel de picante medio-alto.',
    'Cinta de 12 un',
    15.00,
    30,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Lays_Spicy.jpg/320px-Lays_Spicy.jpg',
    12,
    1.70,
    1
),
(
    'DORI001',
    'Doritos',
    'Cinta Doritos Queso Mega',
    'Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Queso Mega de 50g. Snacks crujientes e irresistibles.',
    'Cinta de 12 un',
    18.00,
    40,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Doritos_Nacho_Cheese.jpg/320px-Doritos_Nacho_Cheese.jpg',
    12,
    1.80,
    1
),
(
    'DORI002',
    'Doritos Flamin Hot',
    'Cinta Doritos Flamin Hot',
    'Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Flamin Hot de 50g. Picante extremo para valientes.',
    'Cinta de 12 un',
    19.20,
    35,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Doritos_Flamin%27_Hot.jpg/320px-Doritos_Flamin%27_Hot.jpg',
    12,
    2.00,
    1
),
(
    'DORI003',
    'Doritos',
    'Cinta Doritos Pizza',
    'Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Pizza de 50g. Combinación perfecta de queso y especias.',
    'Cinta de 12 un',
    18.00,
    25,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Doritos_Pizza.jpg/320px-Doritos_Pizza.jpg',
    12,
    1.85,
    1
),
(
    'CHEE001',
    'Cheetos',
    'Cinta Cheetos Crunchy',
    'Cinta conteniendo 12 unidades de Chizitos Horneados Cheetos Crunchy sabor Queso de 40g. La forma divertida de comer queso.',
    'Cinta de 12 un',
    13.20,
    60,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Cheetos.jpg/320px-Cheetos.jpg',
    12,
    1.30,
    1
),
(
    'CHEE002',
    'Cheetos',
    'Cinta Cheetos Puffs',
    'Cinta conteniendo 12 unidades de Snacks Horneados inflados Cheetos sabor Queso de 40g. Textura suave y aireada.',
    'Cinta de 12 un',
    13.20,
    45,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Cheetos_Puffs.jpg/320px-Cheetos_Puffs.jpg',
    12,
    1.30,
    1
),
(
    'CHEE003',
    'Cheetos',
    'Cinta Cheetos Flamin Hot',
    'Cinta conteniendo 12 unidades de Cheetos Horneados sabor Flamin Hot de 40g. Suaves pero muy picantes.',
    'Cinta de 12 un',
    13.20,
    40,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Baked_Cheetos_Flamin_Hot.jpg/320px-Baked_Cheetos_Flamin_Hot.jpg',
    12,
    1.30,
    1
),
(
    'CHTR001',
    'Cheese Tris',
    'Cinta Cheese Tris Original',
    'Tira/Cinta para colgar conteniendo 12 unidades de Cheese Tris sabor Queso de 35g. Un clásico infaltable.',
    'Cinta de 12 un',
    12.00,
    80,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cheese_puffs.jpg/320px-Cheese_puffs.jpg',
    12,
    1.20,
    1
),
(
    'CHTR002',
    'Cheese Tris',
    'Cinta Cheese Tris Picante',
    'Tira/Cinta conteniendo 12 unidades de Cheese Tris sabor Queso Picante de 35g. Toque picante en cada bocado.',
    'Cinta de 12 un',
    12.60,
    50,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Spicy_cheese_puffs.jpg/320px-Spicy_cheese_puffs.jpg',
    12,
    1.25,
    1
),
(
    'CUAT001',
    'Cuates',
    'Cinta Cuates Picantes',
    'Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates Picantes de 40g. Maní crujiente sabor picante y limón.',
    'Cinta de 12 un',
    14.40,
    75,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Roasted_peanuts.jpg/320px-Roasted_peanuts.jpg',
    12,
    1.50,
    1
),
(
    'CUAT002',
    'Cuates',
    'Cinta Cuates Salados',
    'Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates con Sal de 40g. Maní clásico tostado saladito.',
    'Cinta de 12 un',
    14.40,
    65,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Salted_peanuts.jpg/320px-Salted_peanuts.jpg',
    12,
    1.50,
    1
),
(
    'RUF001',
    'Ruffles',
    'Cinta Ruffles Original',
    'Cinta conteniendo 12 unidades de Papas Fritas Onduladas Ruffles de 45g. Ondas que retienen más sabor.',
    'Cinta de 12 un',
    15.60,
    35,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Ruffles_potato_chips_bag.jpg/320px-Ruffles_potato_chips_bag.jpg',
    12,
    1.60,
    1
),
(
    'RUF002',
    'Ruffles',
    'Cinta Ruffles Queso',
    'Cinta conteniendo 12 unidades de Papas Fritas Onduladas sabor Queso Cheddar Ruffles de 45g. Extra quesosas.',
    'Cinta de 12 un',
    15.60,
    30,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Ruffles_Cheddar_and_Sour_Cream_Chips_Bag.jpg/320px-Ruffles_Cheddar_and_Sour_Cream_Chips_Bag.jpg',
    12,
    1.60,
    1
),
(
    'KARI001',
    'Karinto',
    'Cinta Karinto Confitado',
    'Tira/Cinta conteniendo 12 unidades de Maní Confitado Karinto de 50g. Maní dulce tostado artesanal.',
    'Cinta de 12 un',
    12.00,
    55,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Praline_peanuts.jpg/320px-Praline_peanuts.jpg',
    12,
    1.20,
    1
),
(
    'KARI002',
    'Karinto',
    'Cinta Karinto con Pasas',
    'Tira/Cinta conteniendo 12 unidades de Mix de Maní y Pasas Karinto de 50g. Excelente balance de dulce y salado.',
    'Cinta de 12 un',
    13.20,
    40,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Trail_mix_with_peanuts_and_raisins.jpg/320px-Trail_mix_with_peanuts_and_raisins.jpg',
    12,
    1.30,
    1
),
(
    'NATU001',
    'Natuchips',
    'Cinta Natuchips Plátano Sal',
    'Tira/Cinta conteniendo 12 unidades de Plátano Verde Frito con Sal Natuchips de 45g. Snacks naturales y crujientes.',
    'Cinta de 12 un',
    15.00,
    35,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Plantain_chips_plate.jpg/320px-Plantain_chips_plate.jpg',
    12,
    1.50,
    1
),
(
    'NATU002',
    'Natuchips',
    'Cinta Natuchips Plátano Dulce',
    'Tira/Cinta conteniendo 12 unidades de Plátano Maduro Frito Natuchips de 45g. Toque dulce natural.',
    'Cinta de 12 un',
    15.00,
    30,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Sweet_plantain_chips.jpg/320px-Sweet_plantain_chips.jpg',
    12,
    1.50,
    1
),
(
    'TORT001',
    'Tortees',
    'Cinta Tortees Picante',
    'Tira/Cinta conteniendo 12 unidades de Tortillas de Maíz Sabor Picante Tortees de 40g. Sabor mexicano picantito.',
    'Cinta de 12 un',
    12.00,
    45,
    'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Tortilla_chips_with_chili.jpg/320px-Tortilla_chips_with_chili.jpg',
    12,
    1.20,
    1
);

