
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `BUS_BROWSELOG`
-- ----------------------------
DROP TABLE IF EXISTS `BUS_BROWSELOG`;
CREATE TABLE `BUS_BROWSELOG` (
 		`BROWSELOG_ID` varchar(100) NOT NULL,
		`PAGENAME` varchar(255) DEFAULT NULL COMMENT '页面名称',
		`PAGETYPE` varchar(255) DEFAULT NULL COMMENT '类型',
		`OPDATE` varchar(255) DEFAULT NULL COMMENT '日期',
		`OPUSER` varchar(255) DEFAULT NULL COMMENT '操作人',
		`ISDELETE` varchar(255) DEFAULT NULL COMMENT '是否删除',
		`PAGEID` varchar(255) DEFAULT NULL COMMENT '页面ID',
  		PRIMARY KEY (`BROWSELOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
