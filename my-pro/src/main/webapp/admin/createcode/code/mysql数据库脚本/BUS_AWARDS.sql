
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `BUS_AWARDS`
-- ----------------------------
DROP TABLE IF EXISTS `BUS_AWARDS`;
CREATE TABLE `BUS_AWARDS` (
 		`AWARDS_ID` varchar(100) NOT NULL,
		`AWARDSNUMBER` int(11) NOT NULL COMMENT '随机数',
  		PRIMARY KEY (`AWARDS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
