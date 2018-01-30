-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 27, 2017 at 08:55 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `safenet`
--

-- --------------------------------------------------------

--
-- Table structure for table `SCHEDULE_CHECKIN`
--

CREATE TABLE `SCHEDULE_CHECKIN` (
  `ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `HOUR` int(11) NOT NULL,
  `MINUTE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SETTINGS`
--

CREATE TABLE `SETTINGS` (
  `SYMP_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `ID` int(11) NOT NULL,
  `NAME` varchar(15) NOT NULL,
  `TYPE` varchar(3) NOT NULL,
  `VALUE` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SF_EVENTS`
--

CREATE TABLE `SF_EVENTS` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(15) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `COMPLEMENT` varchar(40) NOT NULL,
  `INSTANT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `SF_EVENTS`
--

INSERT INTO `SF_EVENTS` (`ID`, `NAME`, `TYPE`, `COMPLEMENT`, `INSTANT`) VALUES
(1, 'FALL', 5, 'at 22.9068Â° S, 43.1729Â° W', '2017-04-03 19:59:58'),
(2, 'FALL', 5, 'at 22.9068Â° S, 43.1729Â° W', '2017-04-03 20:07:02'),
(3, 'FALL', 5, 'at 22.9068Â° S, 43.1729Â° W', '2017-04-03 20:07:05');

-- --------------------------------------------------------

--
-- Table structure for table `SYMPTOMS`
--

CREATE TABLE `SYMPTOMS` (
  `ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `NAME` varchar(15) NOT NULL,
  `ADD_ENTRY` tinyint(1) NOT NULL,
  `UNIT` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `USERS`
--

CREATE TABLE `USERS` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `PHONE_NUM` varchar(20) NOT NULL,
  `IMEI` int(11) NOT NULL,
  `PAT_OR_GUA` tinyint(1) NOT NULL,
  `ID_GUARDIAN` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `USERS`
--

INSERT INTO `USERS` (`ID`, `NAME`, `PHONE_NUM`, `IMEI`, `PAT_OR_GUA`, `ID_GUARDIAN`) VALUES
(3, 'SERGIO ENG', '5521996030963', 2147483647, 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `SCHEDULE_CHECKIN`
--
ALTER TABLE `SCHEDULE_CHECKIN`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `SETTINGS`
--
ALTER TABLE `SETTINGS`
  ADD PRIMARY KEY (`SYMP_ID`);

--
-- Indexes for table `SF_EVENTS`
--
ALTER TABLE `SF_EVENTS`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD UNIQUE KEY `ID_2` (`ID`);

--
-- Indexes for table `SYMPTOMS`
--
ALTER TABLE `SYMPTOMS`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `USERS`
--
ALTER TABLE `USERS`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `SCHEDULE_CHECKIN`
--
ALTER TABLE `SCHEDULE_CHECKIN`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `SETTINGS`
--
ALTER TABLE `SETTINGS`
  MODIFY `SYMP_ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `SF_EVENTS`
--
ALTER TABLE `SF_EVENTS`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `SYMPTOMS`
--
ALTER TABLE `SYMPTOMS`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `USERS`
--
ALTER TABLE `USERS`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
