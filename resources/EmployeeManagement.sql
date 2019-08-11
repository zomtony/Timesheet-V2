-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 11, 2018 at 09:09 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1
CREATE DATABASE EmployeeManagement;
GRANT ALL PRIVILEGES ON EmployeeManagement.* TO manager@localhost IDENTIFIED BY 'manager';
GRANT ALL PRIVILEGES ON EmployeeManagement.* TO manager@"%" IDENTIFIED BY 'manager';
USE EmployeeManagement;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "-08:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `EmployeeManagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `Credentials`
--

CREATE TABLE `Credentials` (
  `userName` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Credentials`
--

INSERT INTO `Credentials` (`userName`, `password`) VALUES
('BCIT', '123456'),
('admin', '000000'),
('zom', '000000');

-- --------------------------------------------------------

--
-- Table structure for table `EmployeeInfo`
--

CREATE TABLE `EmployeeInfo` (
  `empNumber` int(10) NOT NULL,
  `userName` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `EmployeeInfo`
--

INSERT INTO `EmployeeInfo` (`empNumber`, `userName`, `name`) VALUES
(102, 'BCIT', 'IS'),
(1, 'admin', 'admin'),
(1024, 'zom', 'zom');

-- --------------------------------------------------------

--
-- Table structure for table `Timesheet`
--

CREATE TABLE `Timesheet` (
  `empNumber` int(10) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `weekNumber` int(5) NOT NULL,
  `weekEnding` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Timesheet`
--

INSERT INTO `Timesheet` (`empNumber`, `name`, `weekNumber`, `weekEnding`) VALUES
(102, 'IS', 46, '2018-11-16'),
(1024, 'zom', 46, '2018-11-16');

-- --------------------------------------------------------

--
-- Table structure for table `TimesheetRow`
--

CREATE TABLE `TimesheetRow` (
  `empNumber` int(10) NOT NULL,
  `weekEnding` date NOT NULL,
  `wp` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `project` int(10) NOT NULL,
  `sat` decimal(3,1) DEFAULT NULL,
  `sun` decimal(3,1) DEFAULT NULL,
  `mon` decimal(3,1) DEFAULT NULL,
  `tue` decimal(3,1) DEFAULT NULL,
  `wed` decimal(3,1) DEFAULT NULL,
  `thu` decimal(3,1) DEFAULT NULL,
  `fri` decimal(3,1) DEFAULT NULL,
  `notes` varchar(2000) CHARACTER SET utf32 COLLATE utf32_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `TimesheetRow`
--

INSERT INTO `TimesheetRow` (`empNumber`, `weekEnding`, `wp`, `project`, `sat`, `sun`, `mon`, `tue`, `wed`, `thu`, `fri`, `notes`) VALUES
(102, '2018-11-16', 'A1', 1, '2.0', NULL, NULL, NULL, NULL, NULL, NULL, ''),
(102, '2018-11-16', 'A2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ''),
(102, '2018-11-16', 'A3', 3, NULL, NULL, NULL, NULL, NULL, '2.0', NULL, ''),
(102, '2018-11-16', 'A4', 4, NULL, NULL, NULL, NULL, '4.0', NULL, NULL, ''),
(102, '2018-11-16', 'A5', 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ''),
(102, '2018-11-16', 'A6', 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADD ONE ROW'),
(1024, '2018-11-16', 'A0', 1, '1.0', NULL, NULL, NULL, '6.0', NULL, NULL, ''),
(1024, '2018-11-16', 'A1', 2, NULL, NULL, '6.0', NULL, NULL, NULL, '6.0', 'wOW'),
(1024, '2018-11-16', 'A2', 3, NULL, '5.0', NULL, NULL, NULL, NULL, NULL, ''),
(1024, '2018-11-16', 'A3', 4, NULL, NULL, NULL, '2.0', NULL, NULL, NULL, ''),
(1024, '2018-11-16', 'A7', 5, NULL, NULL, NULL, '6.0', NULL, NULL, NULL, 'edit table'),
(1024, '2018-11-16', 'A8', 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'add one row');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Credentials`
--
ALTER TABLE `Credentials`
  ADD PRIMARY KEY (`userName`);

--
-- Indexes for table `EmployeeInfo`
--
ALTER TABLE `EmployeeInfo`
  ADD PRIMARY KEY (`userName`),
  ADD UNIQUE KEY `empNumber` (`empNumber`);

--
-- Indexes for table `Timesheet`
--
ALTER TABLE `Timesheet`
  ADD PRIMARY KEY (`empNumber`,`weekEnding`);

--
-- Indexes for table `TimesheetRow`
--
ALTER TABLE `TimesheetRow`
  ADD PRIMARY KEY (`empNumber`,`weekEnding`,`wp`,`project`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Credentials`
--
ALTER TABLE `Credentials`
  ADD CONSTRAINT `Credentials_ibfk_1` FOREIGN KEY (`userName`) REFERENCES `EmployeeInfo` (`userName`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
