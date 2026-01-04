DROP DATABASE IF EXISTS `gestocardb`;
CREATE DATABASE  IF NOT EXISTS `gestocardb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gestocardb`;
 
DROP USER IF EXISTS 'java2025'@'localhost';
-- Crear el usuario java2025 con la contraseña Java*2025
CREATE USER 'java2025'@'localhost' IDENTIFIED BY 'Java*2025';
-- Otorgar todos los privilegios en la base de datos sanzgestocar al usuario java2025
GRANT ALL PRIVILEGES ON gestocardb.* TO 'java2025'@'localhost';
-- Aplica los cambios de privilegios


-- Tabla Usuarios

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `idusuario` smallint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidos` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `dni` char(9) DEFAULT NULL,
  `campobaja` char(1) NOT NULL DEFAULT 'F',
  `avatar` varchar(20) NOT NULL DEFAULT 'default-avatar.png',
  `carneconducir` varchar(20) NOT NULL DEFAULT 'default-carne.png',
  PRIMARY KEY (`idusuario`),
  UNIQUE KEY `EMAIL_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `usuarios` WRITE;
-- Creacion del usuario admin
INSERT INTO usuarios (nombre, apellidos, email, password, dni, campobaja, avatar, carneconducir)
VALUES ('admin','gestor','admin@iesalbarregas.es', MD5('admin'), '0892027F','F','default-avatar.png','default-carne.png');
UNLOCK TABLES;

-- Tabla vehiculos

DROP TABLE IF EXISTS `vehiculos`;
CREATE TABLE `vehiculos` (
  `idvehiculo` smallint NOT NULL AUTO_INCREMENT,
  `idusuario` smallint NOT NULL,
  `marca` varchar(20) NOT NULL,
  `modelo` varchar(30) NOT NULL,
  `motor` VARCHAR(10) NOT NULL CHECK (motor IN ('gasolina', 'gasoil', 'eléctrico')),
  `matricula` varchar(8) NOT NULL,
  `cilindrada` varchar(5)  NOT NULL,
  `caballos` varchar(4)  DEFAULT NULL,
  `color` varchar(7)  DEFAULT NULL,
  `fechacompra` date NOT NULL,
  `fechaventa` date DEFAULT NULL,
  `preciocompra` decimal(7,2) NOT NULL,
  `precioventa` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`idvehiculo`),
  UNIQUE KEY `matricula_UNIQUE` (`matricula`),
  KEY `fk_vehiculos_usuarios` (`idusuario`),
  CONSTRAINT `fk_vehiculos_usuarios` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Fotos

DROP TABLE IF EXISTS `fotos`;
CREATE TABLE `fotos` (
  `idfoto` smallint NOT NULL AUTO_INCREMENT,
  `idvehiculo` smallint NOT NULL,
  `foto`BLOB NOT NULL,
  PRIMARY KEY (`idfoto`),
  KEY `fk_fotos_vehiculos` (`idvehiculo`),
  CONSTRAINT `fk_fotos_vehiculos` FOREIGN KEY (`idvehiculo`) REFERENCES `vehiculos` (`idvehiculo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla Gastos

DROP TABLE IF EXISTS `gastos`;
CREATE TABLE `gastos` (
  `idgasto` smallint NOT NULL AUTO_INCREMENT,
  `idvehiculo` smallint NOT NULL,
  `concepto` varchar(40) NOT NULL,
  `fechagasto` date NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `importe` decimal(6,2) NOT NULL,
  `establecimiento` varchar(100) DEFAULT NULL,
  `kilometros` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`idgasto`),
  KEY `fk_gastos_vehiculos` (`idvehiculo`),
  CONSTRAINT `fk_gastos_vehiculos` FOREIGN KEY (`idvehiculo`) REFERENCES `vehiculos` (`idvehiculo`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

select * from fotos;
select * from usuarios;
select * from vehiculos;
select * from gastos;

