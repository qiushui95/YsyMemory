/*
 Navicat Premium Data Transfer

 Source Server         : dev
 Source Server Type    : PostgreSQL
 Source Server Version : 130001
 Source Host           : 192.168.31.115:5432
 Source Catalog        : memory
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130001
 File Encoding         : 65001

 Date: 28/12/2020 12:21:15
*/

-- ----------------------------
-- Table structure for t_upload
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_upload";
CREATE TABLE "public"."t_upload" (
  "c_id" uuid NOT NULL,
  "c_path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "c_width" int2 NOT NULL,
  "c_height" int2 NOT NULL,
  "c_md5" char(32) COLLATE "pg_catalog"."default" NOT NULL,
  "c_create_time" timestamp(6) NOT NULL,
  "c_owner_id" uuid NOT NULL,
  "c_photo_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."t_upload"."c_path" IS '图片链接位置';
COMMENT ON COLUMN "public"."t_upload"."c_width" IS '图片宽度';
COMMENT ON COLUMN "public"."t_upload"."c_height" IS '图片高度';
COMMENT ON COLUMN "public"."t_upload"."c_md5" IS '图片唯一md5';
COMMENT ON COLUMN "public"."t_upload"."c_create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_upload"."c_owner_id" IS '上传者id';
COMMENT ON COLUMN "public"."t_upload"."c_photo_time" IS '拍照时间';

-- ----------------------------
-- Records of t_upload
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_user";
CREATE TABLE "public"."t_user" (
  "c_phone" char(11) COLLATE "pg_catalog"."default" NOT NULL,
  "c_marker" varchar(6) COLLATE "pg_catalog"."default" NOT NULL,
  "c_password" char(32) COLLATE "pg_catalog"."default" NOT NULL,
  "c_id" uuid NOT NULL,
  "c_create_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."t_user"."c_phone" IS '手机号';
COMMENT ON COLUMN "public"."t_user"."c_marker" IS '称呼';
COMMENT ON COLUMN "public"."t_user"."c_password" IS '密码';
COMMENT ON COLUMN "public"."t_user"."c_id" IS '主键id';
COMMENT ON COLUMN "public"."t_user"."c_create_time" IS '创建时间';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO "public"."t_user" VALUES ('13540817567', '爸爸', '1                               ', 'e593086b-b654-48a0-9c1d-51a17f53d271', '2020-12-14 17:04:49.966235');

-- ----------------------------
-- Function structure for auto_insert_create_time
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."auto_insert_create_time"();
CREATE OR REPLACE FUNCTION "public"."auto_insert_create_time"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
begin
	new.c_create_time =current_timestamp;
  return new;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Function structure for auto_insert_id
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."auto_insert_id"();
CREATE OR REPLACE FUNCTION "public"."auto_insert_id"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
begin
		IF (new.c_id is null) THEN
	new.c_id = gen_random_uuid();
ELSE
	new.c_id =new.c_id ;
END IF;
    return new;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Triggers structure for table t_upload
-- ----------------------------
CREATE TRIGGER "upload_auto_create_time" BEFORE INSERT ON "public"."t_upload"
FOR EACH ROW
EXECUTE PROCEDURE "public"."auto_insert_create_time"();
CREATE TRIGGER "upload_auto_id" BEFORE INSERT ON "public"."t_upload"
FOR EACH ROW
EXECUTE PROCEDURE "public"."auto_insert_id"();

-- ----------------------------
-- Uniques structure for table t_upload
-- ----------------------------
ALTER TABLE "public"."t_upload" ADD CONSTRAINT "upload_unique_md5" UNIQUE ("c_md5");

-- ----------------------------
-- Primary Key structure for table t_upload
-- ----------------------------
ALTER TABLE "public"."t_upload" ADD CONSTRAINT "image_pkey" PRIMARY KEY ("c_id");

-- ----------------------------
-- Triggers structure for table t_user
-- ----------------------------
CREATE TRIGGER "user_auto_create_time" BEFORE INSERT ON "public"."t_user"
FOR EACH ROW
EXECUTE PROCEDURE "public"."auto_insert_create_time"();
COMMENT ON TRIGGER "user_auto_create_time" ON "public"."t_user" IS '自动生成创建时间';
CREATE TRIGGER "user_auto_id" BEFORE INSERT ON "public"."t_user"
FOR EACH ROW
EXECUTE PROCEDURE "public"."auto_insert_id"();
COMMENT ON TRIGGER "user_auto_id" ON "public"."t_user" IS '自动生成id';

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "public"."t_user" ADD CONSTRAINT "t_user_pkey" PRIMARY KEY ("c_id");

-- ----------------------------
-- Foreign Keys structure for table t_upload
-- ----------------------------
ALTER TABLE "public"."t_upload" ADD CONSTRAINT "upload_foreign_owner_id" FOREIGN KEY ("c_owner_id") REFERENCES "public"."t_user" ("c_id") ON DELETE CASCADE ON UPDATE CASCADE;
