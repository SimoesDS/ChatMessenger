# ChatMessenger

Este projeto foi desenvolvido com o objetivo educacional para o curso de Ciência da Computação.

## Como funciona?

O ChatMessenger é muito simples, como dá a se entender pelo nome, tem o objetivo de trocar mensagens entre dois hosts ou mais, e para isso faz o uso de Sockets de Berkeley tendo como intermédio um servidor para gerenciar a troca de mensagens e salvá-las em um banco de dados MySQL.

![ChatMessenger](https://github.com/SimoesDS/ChatMessenger/blob/master/ChatMessenger.gif)

## Estrutura do banco de dados

Script para criação do banco de dados em MySQL

~~~sql
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

-- --------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `aps7` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `aps7`;

-- --------------------------------------------------------

CREATE TABLE `messages` (
  `message_id` int(11) DEFAULT NULL,
  `message_type` varchar(255) DEFAULT NULL,
  `message_owner` int(11) DEFAULT NULL,
  `message_receiver` int(11) DEFAULT NULL,
  `message_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

CREATE TABLE `users` (
  `user_id` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_login` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `users` (`user_id`, `user_name`, `user_login`, `user_password`) VALUES
(1, 'Joao', 'joao', '123'),
(2, 'Maria', 'maria', '123');

-- --------------------------------------------------------
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_name`),
  ADD UNIQUE KEY `UK_NOMELOGIN` (`user_name`);

ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
COMMIT;
~~~