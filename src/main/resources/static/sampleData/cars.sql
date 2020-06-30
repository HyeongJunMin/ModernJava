-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.4.12-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 테이블 데이터 test.cars:~90 rows (대략적) 내보내기
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` (`carnumber`, `color`, `brand`, `model`, `max_speed`, `distance`, `height`, `width`, `depth`, `max_fuel`, `fuel`, `price`, `since`, `car_type`, `created_date`, `status`, `created_dt`) VALUES
	('00하3929', 'white', 'Ferrari', 'Excel', 225, 280, 2100, 4485, 1866, 90, 60, 100000000, 2018, 'sedan', NULL, 'repairing', NULL),
	('01가3020', 'white', 'Lamborghini', 'Scoupe', 275, 250, 2650, 4700, 1865, 85, 50, 80000000, 2019, 'sedan', NULL, 'repairing', NULL),
	('01육4939', 'white', 'Lamborghini', 'Elantra', 345, 235, 1247, 4635, 1835, 80, 40, 600000000, 1945, 'sedan', NULL, 'normal', NULL),
	('02나3132', 'purple', 'Maybach', 'UNIVERSE', 270, 200, 1975, 4213, 1598, 90, 51, 25000000, 2007, 'super', NULL, 'normal', NULL),
	('09구9909', 'black', 'Hyundai', 'palisade', 260, 202, 1932, 4730, 2270, 60, 30, 35970000, 2019, 'suv', '20201001', 'normal', NULL),
	('10다3030', 'gold', 'Infiniti', 'TUCSON', 180, 230, 1210, 3500, 1850, 85, 40, 10000000, 1574, 'suv', NULL, 'breakdown', NULL),
	('10육5482', 'gold', 'Toyota', 'NEXO', 180, 210, 1239, 4988, 1900, 90, 70, 150000000, 1958, 'suv', NULL, 'breakdown', NULL),
	('10하1123', 'gold', 'Toyota', 'KONA', 190, 150, 2103, 4234, 1650, 100, 60, 30000000, 2007, 'suv', NULL, 'breakdown', NULL),
	('10하2030', 'black', 'Toyota', 'GRANDEUR', 150, 300, 1302, 4165, 1800, 100, 40, 230000000, 1994, 'coupe', NULL, 'normal', NULL),
	('10하3939', 'yellow', 'Lamborghini', 'Marcia', 350, 250, 900, 4242, 1855, 75, 30, 700000000, 2020, 'sedan', NULL, 'normal', NULL),
	('111통1111', 'red', 'lamborghini', 'aventador', 351, 39110, 1136, 4780, 1720, 90, 71, 758460000, 2020, 'coupe', '20201111', 'normal', NULL),
	('119양9597', 'gray', 'KIA', 'sorento', 260, 1792, 1894, 4880, 2328, 60, 30, 46200000, 2010, 'suv', '20160203', 'normal', NULL),
	('11가5466', 'red', 'Porsche', 'PAVISE', 180, 215, 1256, 4897, 1751, 100, 55, 53000000, 2009, 'super', NULL, 'breakdown', NULL),
	('11고3929', 'silver', 'Audi', 'Pony', 300, 260, 1604, 5500, 2050, 200, 90, 21000000, 2014, 'sedan', NULL, 'repairing', NULL),
	('11라6754', 'silver', 'Ferrari', 'Granada', 240, 250, 1887, 4320, 2200, 70, 80, 13000000, 2015, 'sedan', NULL, 'repairing', NULL),
	('11러5322', 'white', 'Rolls-Royce', 'Phantom', 250, 203, 1656, 5982, 2018, 90, 35, 740000000, 2017, 'sedan', '42751', 'normal', NULL),
	('11러5323', 'white', 'Rolls-Royce', 'Phantom', 250, 208, 1656, 5982, 2018, 90, 36, 740000000, 2017, 'sedan', '42751', 'normal', NULL),
	('11러5324', 'black', 'Rolls-Royce', 'Phantom', 250, 214, 1656, 5982, 2018, 90, 37, 740000000, 2017, 'sedan', '42773', 'normal', NULL),
	('11러5325', 'black', 'Rolls-Royce', 'Phantom', 250, 220, 1656, 5982, 2018, 90, 38, 740000000, 2017, 'sedan', '42773', 'normal', NULL),
	('11사4993', 'green', 'Acura', 'COUNTY', 220, 165, 1584, 3500, 1850, 90, 30, 60000000, 2002, 'suv', NULL, 'repairing', NULL),
	('11자9900', 'red', 'Porsche', 'XCIENT', 170, 195, 1321, 4653, 1789, 100, 56, 65000000, 2010, 'super', NULL, 'breakdown', NULL),
	('11하3929', 'green', 'Acura', 'AEROTOWN', 300, 160, 1567, 3598, 1808, 85, 20, 70000000, 2003, 'super', NULL, 'normal', NULL),
	('12가3030', 'black', 'Lexus', 'AVANTE', 210, 200, 1100, 4475, 1850, 80, 50, 320000000, 1995, 'coupe', NULL, 'normal', NULL),
	('12고3929', 'pink', 'Lexus', 'UNICITY', 250, 120, 1324, 3989, 1624, 70, 50, 90000000, 2005, 'super', NULL, 'normal', NULL),
	('12나3921', 'gray', 'Toyota', 'Starex', 200, 220, 2493, 4000, 1500, 98, 50, 40000000, 2018, 'suv', NULL, 'repairing', NULL),
	('12허1123', 'blue', 'Datsun', 'PALISADE', 320, 300, 2103, 4222, 1810, 80, 50, 250000000, 1998, 'suv', NULL, 'breakdown', NULL),
	('13고2931', 'blue', 'Lamborghini', 'Gallardo', 325, 250, 1136, 4797, 2030, 85, 50, 324000000, 2013, 'coupe', '41395', 'normal', NULL),
	('13고2932', 'yellow', 'Lamborghini', 'Gallardo', 325, 220, 1136, 4797, 2030, 85, 44, 324000000, 2013, 'coupe', '41395', 'normal', NULL),
	('13고2933', 'yellow', 'Lamborghini', 'Gallardo', 325, 225, 1136, 4797, 2030, 85, 45, 324000000, 2013, 'coupe', '41395', 'normal', NULL),
	('13고2934', 'red', 'Lamborghini', 'Gallardo', 325, 230, 1136, 4797, 2030, 85, 46, 324000000, 2013, 'coupe', '41395', 'repairing', NULL),
	('13고2935', 'blue', 'Lamborghini', 'Gallardo', 325, 235, 1136, 4797, 2030, 85, 47, 356000000, 2008, 'roadster', '39600', 'breakdown', NULL),
	('13고2936', 'black', 'Lamborghini', 'Gallardo', 325, 240, 1136, 4797, 2030, 85, 48, 356000000, 2008, 'roadster', '39600', 'normal', NULL),
	('13고2937', 'yellow', 'Lamborghini', 'Gallardo', 325, 245, 1136, 4797, 2030, 85, 49, 356000000, 2008, 'roadster', '39661', 'normal', NULL),
	('13고2938', 'red', 'Lamborghini', 'Gallardo', 325, 245, 1136, 4797, 2030, 85, 49, 356000000, 2008, 'roadster', '39661', 'breakdown', NULL),
	('13육4860', 'red', 'Porsche', 'i10', 150, 190, 1000, 3989, 1985, 120, 70, 55000000, 2011, 'sedan', NULL, 'breakdown', NULL),
	('13자4020', 'silver', 'Ferrari', 'Stellar', 260, 270, 2048, 4658, 1800, 50, 70, 10000000, 2016, 'sedan', NULL, 'repairing', NULL),
	('14가2391', 'red', 'Audi', 'ix25', 190, 185, 1577, 3751, 2020, 110, 85, 65000000, 2012, 'sedan', NULL, 'breakdown', NULL),
	('16공3020', 'red', 'Audi', 'Cortina', 225, 180, 1699, 3198, 1878, 170, 100, 12000000, 2013, 'sedan', NULL, 'breakdown', NULL),
	('16다5342', 'yellow', 'Mclaren', 'Tiburon', 320, 215, 2157, 4323, 1957, 450, 200, 900000000, 2002, 'sedan', NULL, 'normal', NULL),
	('16육8864', 'gray', 'Acura', 'SOLATI', 200, 170, 1343, 4500, 1950, 95, 50, 50000000, 2001, 'suv', NULL, 'repairing', NULL),
	('17하1235', 'pink', 'Lexus', 'PORTER', 260, 100, 1979, 3874, 1478, 50, 50, 24000000, 2006, 'super', NULL, 'normal', NULL),
	('18넘1818', 'white', 'nissan', 'maxima', 260, 48000, 1380, 4570, 2159, 45, 13, 51900000, 2018, 'sedan', '20180815', 'repairing', NULL),
	('19다1008', 'yellow', 'FORD', 'MUSTANG', 280, 17230, 1400, 4790, 1915, 65, 49, 67000000, 2016, 'convertible', '20160321', 'normal', NULL),
	('20국3929', 'black', 'Honda', 'SONATA', 225, 250, 1302, 4897, 1880, 80, 60, 20000000, 1996, 'coupe', NULL, 'normal', NULL),
	('21하7477', 'red', 'DEAWOO', 'TICO', 140, 273000, 1395, 3340, 1400, 33, 7, 1950000, 1996, 'compactcar', '19991231', 'breakdown', NULL),
	('23허9293', 'green', 'Lexus', 'GreenCity', 310, 350, 1978, 3700, 1782, 80, 50, 80000000, 2004, 'super', NULL, 'normal', NULL),
	('255루5657', 'blue', 'chevrolet', 'COLORADO', 340, 1004, 1936, 5010, 2340, 70, 39, 38590000, 2019, 'pickuptruck', '20200117', 'normal', NULL),
	('29파9439', 'black', 'mclaren', 'speedtail', 403, 4602, 1146, 4703, 1810, 90, 39, 256990000, 2018, 'coupe', '20990119', 'normal', NULL),
	('31사3929', 'purple', 'Maybach', 'MIGHTY', 140, 250, 1778, 4887, 1625, 85, 52, 10000000, 2008, 'super', NULL, 'normal', NULL),
	('31재3031', 'olive', 'Audi', 'Q7', 330, 50009, 1688, 4648, 2102, 54, 11, 89620000, 2020, 'suv', '20200611', 'normal', NULL),
	('32다1244', 'white', 'Lamborghini', 'Aventador', 350, 117, 1136, 4943, 2098, 85, 23, 575000000, 2011, 'coupe', '40655', 'normal', NULL),
	('32다1245', 'blue', 'Lamborghini', 'Aventador', 350, 122, 1136, 4943, 2098, 85, 24, 575000000, 2011, 'coupe', '40655', 'repairing', NULL),
	('32다1246', 'yellow', 'Lamborghini', 'Aventador', 350, 127, 1136, 4943, 2098, 85, 25, 575000000, 2011, 'coupe', '40685', 'normal', NULL),
	('32다1249', 'green', 'Lamborghini', 'Aventador', 350, 142, 1136, 4943, 2098, 85, 28, 575000000, 2012, 'coupe', '41073', 'repairing', NULL),
	('32다1250', 'gold', 'Lamborghini', 'Aventador', 350, 147, 1136, 4943, 2098, 85, 29, 575000000, 2012, 'coupe', '41126', 'normal', NULL),
	('32다1251', 'white', 'Lamborghini', 'Aventador', 350, 153, 1136, 4943, 2098, 85, 30, 575000000, 2011, 'roadster', '40655', 'breakdown', NULL),
	('32다1252', 'blue', 'Lamborghini', 'Aventador', 350, 158, 1136, 4943, 2098, 85, 31, 575000000, 2011, 'roadster', '40685', 'normal', NULL),
	('32다1253', 'yellow', 'Lamborghini', 'Aventador', 350, 163, 1136, 4943, 2098, 85, 32, 575000000, 2013, 'roadster', '41539', 'normal', NULL),
	('34수3872', 'blue', 'Hyundai', 'AVANTE', 200, 0, 1482, 4460, 1932, 42, 5, 23050000, 2010, 'sedan', '20110719', 'repairing', NULL),
	('36새2102', 'gray', 'volvo', 'V90', 220, 8330, 1482, 4810, 2020, 60, 29, 67700000, 2020, 'hatchback', '20210625', 'normal', NULL),
	('37여0337', 'white', 'Hyundai', 'sonata', 210, 195860, 1422, 4590, 1878, 40, 29, 15600000, 2005, 'sedan', '20090105', 'repairing', NULL),
	('403송5497', 'brown', 'KIA', 'granbird', 220, 100000, 3650, 13550, 3100, 200, 99, 163000000, 2002, 'compactcar', '20060918', 'normal', NULL),
	('419명1212', 'black', 'MercedesBenz', 'GLS', 280, 0, 1840, 4760, 2030, 60, 0, 132000000, 2018, 'suv', '20190801', 'normal', NULL),
	('43섬3929', 'yellow', 'Mclaren', 'Dynasty', 330, 210, 1145, 4878, 2000, 120, 10, 100000000, 1988, 'sedan', NULL, 'normal', NULL),
	('44표1199', 'red', 'nissan', 'altima', 250, 32000, 1410, 4699, 2104, 52, 26, 49700000, 2019, 'sedan', '20200101', 'normal', NULL),
	('49사3940', 'black', 'Rolls-Royce', 'Wraith', 250, 17020, 1507, 5285, 1947, 82, 22, 401000000, 2016, 'coupe', '20231010', 'repairing', NULL),
	('52모3156', 'blue', 'Bugatti', 'Chiron', 420, 324, 1212, 4544, 2038, 100, 72, 3021760000, 2016, 'super', '42411', 'normal', NULL),
	('52모3157', 'blue', 'Bugatti', 'Chiron', 420, 328, 1212, 4544, 2038, 100, 73, 3021760000, 2016, 'super', '42411', 'normal', NULL),
	('52모3158', 'white', 'Bugatti', 'Chiron', 420, 333, 1212, 4544, 2038, 100, 74, 3021760000, 2016, 'super', '42411', 'normal', NULL),
	('52모3159', 'white', 'Bugatti', 'Chiron', 420, 337, 1212, 4544, 2038, 100, 75, 3021760000, 2016, 'super', '42449', 'normal', NULL),
	('52모3160', 'green', 'Bugatti', 'Chiron', 420, 342, 1212, 4544, 2038, 100, 76, 3021760000, 2016, 'super', '42449', 'normal', NULL),
	('52소4255', 'green', 'Audi', 'A5', 310, 11, 1463, 4600, 2174, 50, 8, 121000000, 2020, 'coupe', '20200502', 'normal', NULL),
	('53오6875', 'black', 'KIA', 'K5', 220, 66000, 1480, 4520, 1900, 45, 28, 31000000, 2020, 'sedan', '20200612', 'normal', NULL),
	('57머7942', 'yellow', 'fiat', 'Spider', 240, 98000, 1390, 4460, 1990, 45, 29, 5890000, 2016, 'convertible', '20161209', 'breakdown', NULL),
	('58광1358', 'brown', 'volvo', 'XC40', 250, 7622, 1847, 4854, 2304, 50, 12, 37990000, 2009, 'suv', '20121111', 'normal', NULL),
	('59차1234', 'white', 'Bentley', 'Mulsanne', 296, 29000, 1521, 5575, 1926, 96, 88, 499990000, 2017, 'sedan', '19991301', 'normal', NULL),
	('61구1212', 'black', 'Hyundai', 'starex', 240, 33300, 2140, 6150, 2210, 62, 58, 127000000, 2017, 'van', '20181101', 'breakdown', NULL),
	('66푸8777', 'white', 'Volkswagen', 'Touareg', 230, 15, 1600, 4550, 1978, 50, 10, 88400000, 2019, 'suv', '20190930', 'normal', NULL),
	('74호8819', 'black', 'KIA', 'K7', 240, 1500, 1490, 4600, 1980, 45, 30, 38500000, 2018, 'sedan', '20181211', 'normal', NULL),
	('777짱7979', 'blue', 'Ferrari', '488Spider', 325, 16, 1215, 4570, 1955, 60, 54, 315000000, 2016, 'coupe', '20210226', 'normal', NULL),
	('82서3498', 'red', 'Ssangyong', 'TIVOLI', 190, 92000, 1620, 4225, 1810, 40, 27, 23790000, 2015, 'suv', '20191123', 'normal', NULL),
	('85루0102', 'blue', 'BMW', 'X6', 250, 5020, 1689, 4935, 2005, 55, 43, 105600000, 2022, 'suv', '20110409', 'normal', NULL),
	('86채7555', 'gray', 'KIA', 'K3', 210, 42900, 1450, 4360, 1880, 42, 42, 24500000, 2019, 'sedan', '20200131', 'normal', NULL),
	('87크7777', 'white', 'volvo', 'S60', 250, 365, 1422, 4696, 2188, 50, 49, 43800000, 2026, 'sedan', '20400315', 'normal', NULL),
	('89누6623', 'silver', 'Bentley', 'Bentayga', 290, 351, 1742, 5140, 1998, 85, 51, 285000000, 2018, 'suv', '43302', 'repairing', NULL),
	('89누6624', 'silver', 'Bentley', 'Bentayga', 290, 358, 1742, 5140, 1998, 85, 52, 285000000, 2018, 'suv', '43302', 'breakdown', NULL),
	('89누6625', 'black', 'Bentley', 'Bentayga', 290, 365, 1742, 5140, 1998, 85, 53, 285000000, 2018, 'suv', '43377', 'normal', NULL),
	('89누6626', 'white', 'Bentley', 'Bentayga', 290, 372, 1742, 5140, 1998, 85, 54, 285000000, 2018, 'suv', '43799', 'normal', NULL),
	('89누6627', 'white', 'Bentley', 'Bentayga', 290, 379, 1742, 5140, 1998, 85, 55, 285000000, 2018, 'suv', '43799', 'normal', NULL),
	('93오9840', 'white', 'Hyundai', 'SANTAFE', 220, 53200, 2010, 4790, 2110, 60, 29, 42890000, 2019, 'suv', '20200228', 'normal', NULL);
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
