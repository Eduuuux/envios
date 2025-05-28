-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-05-2025 a las 07:45:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `envios`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `apellido`, `correo`, `direccion`, `nombre`, `telefono`) VALUES
(1, 'García', 'juan.garcia@email.com', 'Av. Siempre Viva 123', 'Juan', '5551234567'),
(2, 'Martínez', 'ana.martinez@email.com', 'Calle Luna 456', 'Ana', '5552345678'),
(3, 'López', 'carlos.lopez@email.com', 'Boulevard Sol 789', 'Carlos', '5553456789'),
(4, 'Pérez', 'maria.perez@email.com', 'Calle Estrella 101', 'Maria', '5554567890'),
(5, 'Sánchez', 'luis.sanchez@email.com', 'Av. Central 202', 'Luis', '5555678901'),
(6, 'Ramírez', 'laura.ramirez@email.com', 'Calle Norte 303', 'Laura', '5556789012'),
(7, 'Torres', 'jose.torres@email.com', 'Av. Sur 404', 'Jose', '5557890123');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `envio`
--

CREATE TABLE `envio` (
  `id_envio` int(11) NOT NULL,
  `direccion_destino` varchar(252) NOT NULL,
  `estado` enum('DESCONOCIDO','ENVIADO','EN_PROCESO','RECIBIDO') NOT NULL,
  `fecha_entrega` varchar(255) DEFAULT NULL,
  `fecha_envio` varchar(255) NOT NULL,
  `numero_envio` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `paquete_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `envio`
--

INSERT INTO `envio` (`id_envio`, `direccion_destino`, `estado`, `fecha_entrega`, `fecha_envio`, `numero_envio`, `cliente_id`, `paquete_id`) VALUES
(1, 'Calle 10 #123, Ciudad', 'EN_PROCESO', '2025-05-29', '2025-05-27', 1001, 1, 3),
(3, 'Boulevard Sur 789, Ciudad', 'RECIBIDO', '2025-05-26', '2025-05-24', 1003, 3, 7),
(4, 'Calle Norte 321, Ciudad', 'EN_PROCESO', '2025-05-30', '2025-05-27', 1004, 4, 9),
(5, 'Av. Este 654, Ciudad', 'ENVIADO', '2025-05-29', '2025-05-26', 1005, 5, 11),
(6, 'Calle 11 #124, Ciudad', 'ENVIADO', '2025-05-30', '2025-05-28', 1006, 1, 1),
(7, 'Calle 12 #125, Ciudad', 'RECIBIDO', '2025-05-31', '2025-05-29', 1007, 1, 2),
(8, 'Boulevard Norte 890, Ciudad', 'EN_PROCESO', '2025-06-01', '2025-05-30', 1008, 3, 4),
(9, 'Av. Siempre Viva 123', 'ENVIADO', '2025-05-30', '2025-05-28', 1006123, 1, 10),
(11, 'Av. Siempre Viva 123', 'ENVIADO', '2025-05-30', '2025-05-28', 100612113, 1, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paquete`
--

CREATE TABLE `paquete` (
  `id` int(11) NOT NULL,
  `peso` double NOT NULL,
  `producto` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `paquete`
--

INSERT INTO `paquete` (`id`, `peso`, `producto`) VALUES
(1, 0.5, 'Chanel No. 5'),
(2, 0.7, 'Dior Sauvage'),
(3, 1, 'Acqua di Gio'),
(4, 0.6, 'La Vie Est Belle'),
(5, 0.8, 'Light Blue'),
(6, 1.2, 'Invictus'),
(7, 0.9, '212 VIP'),
(8, 1.1, 'CK One'),
(9, 0.4, 'Eros'),
(10, 0.5, 'Good Girl'),
(11, 0.6, 'One Million'),
(12, 0.7, 'L’Interdit'),
(13, 1, 'Bleu de Chanel'),
(14, 0.8, 'Black Opium'),
(15, 0.9, 'Flowerbomb');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `envio`
--
ALTER TABLE `envio`
  ADD PRIMARY KEY (`id_envio`),
  ADD UNIQUE KEY `UKpkinvywh6jujp2mtygrobylij` (`numero_envio`),
  ADD KEY `FK1f0l5tpqdapkvbiw574i2kl2p` (`cliente_id`),
  ADD KEY `FKasek3o5gve4omcp66wco5ct6i` (`paquete_id`);

--
-- Indices de la tabla `paquete`
--
ALTER TABLE `paquete`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `envio`
--
ALTER TABLE `envio`
  MODIFY `id_envio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `paquete`
--
ALTER TABLE `paquete`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `envio`
--
ALTER TABLE `envio`
  ADD CONSTRAINT `FK1f0l5tpqdapkvbiw574i2kl2p` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FKasek3o5gve4omcp66wco5ct6i` FOREIGN KEY (`paquete_id`) REFERENCES `paquete` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
