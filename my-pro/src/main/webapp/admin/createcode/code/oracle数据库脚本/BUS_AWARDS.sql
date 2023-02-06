-- ----------------------------
-- Table structure for "C##FHBOOT"."BUS_AWARDS"
-- ----------------------------
-- DROP TABLE "C##FHBOOT"."BUS_AWARDS";
CREATE TABLE "C##FHBOOT"."BUS_AWARDS" (
	"AWARDSNUMBER" NUMBER(11) NULL ,
	"AWARDS_ID" VARCHAR2(100 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE
;

COMMENT ON COLUMN "C##FHBOOT"."BUS_AWARDS"."AWARDSNUMBER" IS '随机数';
COMMENT ON COLUMN "C##FHBOOT"."BUS_AWARDS"."AWARDS_ID" IS 'ID';

-- ----------------------------
-- Indexes structure for table BUS_AWARDS
-- ----------------------------

-- ----------------------------
-- Checks structure for table "C##FHBOOT"."BUS_AWARDS"

-- ----------------------------

ALTER TABLE "C##FHBOOT"."BUS_AWARDS" ADD CHECK ("AWARDS_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "C##FHBOOT"."BUS_AWARDS"
-- ----------------------------
ALTER TABLE "C##FHBOOT"."BUS_AWARDS" ADD PRIMARY KEY ("AWARDS_ID");
