-- ==========================================
-- BASE DE DATOS APP CLIENTES YAZUMI
-- MYSQL
-- ==========================================
drop database yazumi_app;
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
unidades_por_paquete INT DEFAULT 1,
precio_sugerido DECIMAL(10,2) DEFAULT 0.00,
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
'$2a$10$2ihckjfO.TQ3nexVdSVaVeATCop8B2P4zA/odGsRLhyufVb98EJse',
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
)VALUES
(
    'LAYS001',
    'Lays',
    'Cinta Lays Clásicas',
    'Cinta conteniendo 12 unidades de Papas Fritas Clásicas Lay\'s de 45g. Las preferidas de todos, hechas con papas peruanas seleccionadas.',
    'Cinta de 12 un',
    14.40,
    50,
    'images/lays_clasicas.png',
    12,
    1.50,
    1
),
(
    'LAYS002',
    'Lays',
    'Cinta Lays Pollo a la Brasa',
    'Cinta conteniendo 12 unidades de Papas Fritas Lay\'s sabor Pollo a la Brasa de 45g. El auténtico sabor de nuestro plato bandera peruano.',
    'Cinta de 12 un',
    14.40,
    45,
    'images/lays_pollo.png',
    12,
    1.50,
    1
),
(
    'LAYS003',
    'Lays',
    'Cinta Lays Ondas Picantes',
    'Cinta conteniendo 12 unidades de Papas Fritas Lay\'s Ondas sabor Picante de 45g. Onduladas y con el picante perfecto.',
    'Cinta de 12 un',
    14.40,
    40,
    'images/lays_ondas_picantes.png',
    12,
    1.60,
    1
),
(
    'DORI001',
    'Doritos',
    'Cinta Doritos Queso Atrevido',
    'Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Queso Atrevido de 50g. Sabor intenso y crujiente para los más atrevidos.',
    'Cinta de 12 un',
    18.00,
    40,
    'images/doritos_queso.png',
    12,
    1.80,
    1
),
(
    'DORI002',
    'Doritos',
    'Cinta Doritos Flamin Hot',
    'Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Flamin Hot de 50g. Picante extremo e irresistible.',
    'Cinta de 12 un',
    19.20,
    35,
    'images/doritos_flamin.png',
    12,
    2.00,
    1
),
(
    'DORI003',
    'Doritos',
    'Cinta Doritos Dinamita',
    'Cinta conteniendo 12 unidades de Doritos Dinamita sabor Flamin Hot de 50g. Tortillas enrolladas con picante extremo.',
    'Cinta de 12 un',
    18.00,
    25,
    'images/doritos_dinamita.png',
    12,
    1.80,
    1
),
(
    'CHEE001',
    'Cheetos',
    'Cinta Cheetos Chizitos',
    'Cinta conteniendo 12 unidades de Cheetos Horneados sabor Queso de 40g (los clásicos Chizitos de toda la vida).',
    'Cinta de 12 un',
    13.20,
    60,
    'images/cheetos_chizitos.png',
    12,
    1.30,
    1
),
(
    'CHEE002',
    'Cheetos',
    'Cinta Cheetos Colmillos',
    'Cinta conteniendo 12 unidades de Cheetos Colmillos sabor Queso y Toque de Ají de 40g. Forma divertida y deliciosa.',
    'Cinta de 12 un',
    13.20,
    45,
    'images/cheetos_colmillos.png',
    12,
    1.30,
    1
),
(
    'PIQ001',
    'Piqueo Snax',
    'Cinta Piqueo Snax Original',
    'Cinta conteniendo 12 unidades de Piqueo Snax de 50g. La combinación perfecta de Lays, Doritos, Chizitos y Tocino.',
    'Cinta de 12 un',
    18.00,
    50,
    'images/piqueo_snax.png',
    12,
    1.80,
    1
),
(
    'CUAT001',
    'Cuates',
    'Cinta Cuates Picante',
    'Tira/Cinta conteniendo 12 unidades de Hojuelas de Maíz Cuates sabor Picante de 35g. Las clásicas tortillas picantes peruanas.',
    'Cinta de 12 un',
    12.00,
    75,
    'images/cuates_picante.png',
    12,
    1.20,
    1
),
(
    'CUAT002',
    'Cuates',
    'Cinta Cuates Limón',
    'Tira/Cinta conteniendo 12 unidades de Hojuelas de Maíz Cuates sabor Limón de 35g. Sabor cítrico e intenso.',
    'Cinta de 12 un',
    12.00,
    65,
    'images/cuates_limon.png',
    12,
    1.20,
    1
),
(
    'RUF001',
    'Ruffles',
    'Cinta Ruffles Original',
    'Cinta conteniendo 12 unidades de Papas Fritas Onduladas Ruffles de 45g. Crujientes papas onduladas con sal.',
    'Cinta de 12 un',
    15.60,
    35,
    'images/ruffles_original.png',
    12,
    1.60,
    1
),
(
    'KARI001',
    'Karinto',
    'Cinta Karinto Maní Confitado',
    'Tira/Cinta conteniendo 12 unidades de Maní Confitado Karinto de 45g. El delicioso maní dulce tostado en almíbar.',
    'Cinta de 12 un',
    12.00,
    55,
    'images/karinto_confitado.png',
    12,
    1.20,
    1
),
(
    'KARI002',
    'Karinto',
    'Cinta Karinto Habas con Sal',
    'Tira/Cinta conteniendo 12 unidades de Habas Fritas Saladas Karinto de 45g. Ricas habas crocantes saladitas.',
    'Cinta de 12 un',
    12.00,
    40,
    'images/karinto_habas.png',
    12,
    1.20,
    1
),
(
    'NATU001',
    'Natuchips',
    'Cinta Natuchips Platanitos Sal',
    'Tira/Cinta conteniendo 12 unidades de Plátano Verde Frito con Sal Natuchips de 45g. Crujientes platanitos naturales.',
    'Cinta de 12 un',
    15.00,
    35,
    'images/natuchips_platanitos.png',
    12,
    1.50,
    1
),
(
    'TORT001',
    'Tortees',
    'Cinta Tortees Picante',
    'Tira/Cinta conteniendo 12 unidades de Tortillas de Maíz sabor Picante Tortees de 40g. Crujientes y deliciosas.',
    'Cinta de 12 un',
    12.00,
    45,
    'images/tortees_picante.png',
    12,
    1.20,
    1
);

