-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: energybigdatacommunity
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chart_data`
--

DROP TABLE IF EXISTS `chart_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chart_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `week` varchar(45) DEFAULT NULL,
  `value` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chart_data`
--

LOCK TABLES `chart_data` WRITE;
/*!40000 ALTER TABLE `chart_data` DISABLE KEYS */;
INSERT INTO `chart_data` VALUES (1,'张三','Mon',100),(2,'张三','Tue',130),(3,'张三','Wed',160),(4,'张三','Thu',204),(5,'张三','Fri',210),(6,'张三','Sat',220),(7,'张三','Sun',100),(8,'李四','Mon',400),(9,'李四','Tue',500),(10,'李四','Wed',299),(11,'李四','Thu',300),(12,'李四','Fri',210),(13,'李四','Sat',220),(14,'李四','Sun',330);
/*!40000 ALTER TABLE `chart_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload`
--

DROP TABLE IF EXISTS `file_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_upload` (
  `id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(45) DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `upload_user` varchar(20) DEFAULT NULL,
  `file_path` varchar(100) DEFAULT NULL,
  `file_info` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload`
--

LOCK TABLES `file_upload` WRITE;
/*!40000 ALTER TABLE `file_upload` DISABLE KEYS */;
INSERT INTO `file_upload` VALUES (3,'222','2024-06-10 15:38:45','admin','http://localhost:8082/upload/20240610_15371526.png','<p><b>34er</b><span style=\"font-size: var(--font-size); color: var(--el-text-color-primary);\">?</span></p>');
/*!40000 ALTER TABLE `file_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `references_data`
--

DROP TABLE IF EXISTS `references_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `references_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(50) DEFAULT NULL,
  `publish_year` int DEFAULT NULL,
  `publisher` varchar(50) DEFAULT NULL,
  `cite_str` text,
  `team` varchar(20) DEFAULT NULL,
  `detail` longtext,
  `file_path` varchar(45) DEFAULT NULL,
  `upload_user` varchar(20) DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `view_num` int DEFAULT '0',
  `download_num` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `references_data`
--

LOCK TABLES `references_data` WRITE;
/*!40000 ALTER TABLE `references_data` DISABLE KEYS */;
INSERT INTO `references_data` VALUES (1,'A Survey of Deep Network Solutions for Learning Control in Robotics: From Reinforcement to Imitation','L Tai ， J Zhang ， M Liu ',2013,'aa','[1] Tai L , Zhang J , Liu M ,et al.A Survey of Deep Network Solutions for Learning Control in Robotics: From Reinforcement to Imitation[J]. 2016.DOI:10.48550/arXiv.1612.07139','AI',NULL,NULL,NULL,NULL,0,0),(2,'A Comprehensive Survey of Multiagent Reinforcement Learning','L Busoniu，R Babuska，B De Schutter',2023,'aa','[1] Busoniu L , Babuska R , De Schutter B .A Comprehensive Survey of Multiagent Reinforcement Learning[J].IEEE Transactions on Systems Man & Cybernetics Part C, 2008, 38(2):156-172.DOI:10.1109/TSMCC.2007.913919.','AI',NULL,NULL,NULL,NULL,0,0),(3,'A Comprehensive ccce','L Busoniu，R Babuska，B De Schutter',2024,NULL,NULL,NULL,NULL,NULL,'admin','2024-06-07 09:31:32',0,0);
/*!40000 ALTER TABLE `references_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userid` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `deleted` tinyint DEFAULT NULL,
  `locked` tinyint DEFAULT NULL,
  `user_role` varchar(15) DEFAULT NULL,
  `team` varchar(20) DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `id_UNIQUE` (`userid`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'小孙','111','e10adc3949ba59abbe56e057f20f883e',0,0,'student','BI;AI',NULL),(2,'张老师','222','e10adc3949ba59abbe56e057f20f883e',0,0,'teacher','AI;MIS;ERP',NULL),(3,'am admin','admin','698d51a19d8a121ce581499d7b701668',0,0,'admin',NULL,NULL),(4,'3333','33','e10adc3949ba59abbe56e057f20f883e',0,0,NULL,NULL,'2024-05-15 22:47:50'),(5,'4444','444','e10adc3949ba59abbe56e057f20f883e',0,0,NULL,NULL,'2024-05-15 22:58:21'),(6,'222','bbb','e10adc3949ba59abbe56e057f20f883e',0,0,'student',NULL,'2024-06-07 09:14:40'),(7,'555','555','e10adc3949ba59abbe56e057f20f883e',0,0,'student',NULL,'2024-06-07 09:17:44');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `userinfoid` int NOT NULL AUTO_INCREMENT,
  `userid` int NOT NULL,
  `username` varchar(20) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `default_flag` tinyint DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `introduce` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`userinfoid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_token`
--

DROP TABLE IF EXISTS `user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_token` (
  `userid` int NOT NULL,
  `token` varchar(32) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_token`
--

LOCK TABLES `user_token` WRITE;
/*!40000 ALTER TABLE `user_token` DISABLE KEYS */;
INSERT INTO `user_token` VALUES (3,'22de55d1952c0684d3f7db60aa213cb9','2024-06-12 23:12:42','2024-06-14 23:12:42');
/*!40000 ALTER TABLE `user_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'energybigdatacommunity'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-13  0:09:43
