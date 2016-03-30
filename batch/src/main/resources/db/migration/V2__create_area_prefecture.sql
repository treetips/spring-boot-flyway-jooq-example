drop table if exists area;
create table area (
	area_cd tinyint not null comment 'エリアコード'
	,area_name varchar(191) not null comment 'エリア名'
	,primary key (area_cd)
) engine=innodb charset=utf8mb4 row_format=dynamic comment='エリアマスタ';

insert into area values
(1,'北海道'),
(2,'東北'),
(3,'関東'),
(4,'中部'),
(5,'近畿'),
(6,'中国'),
(7,'四国'),
(8,'九州')
;

drop table if exists prefecture;
create table prefecture (
	area_cd tinyint not null comment 'エリアコード'
	,prefecture_cd tinyint not null comment '都道府県コード'
	,prefecture_name varchar(191) not null comment '都道府県名'
	,primary key (prefecture_cd)
) engine=innodb charset=utf8mb4 row_format=dynamic comment='都道府県マスタ';

alter table prefecture add key idx1(area_cd, prefecture_cd) comment '都道府県検索';

insert into prefecture values
(1,1,'北海道'),
(2,2,'青森県'),
(2,3,'岩手県'),
(2,4,'宮城県'),
(2,5,'秋田県'),
(2,6,'山形県'),
(2,7,'福島県'),
(3,8,'茨城県'),
(3,9,'栃木県'),
(3,10,'群馬県'),
(3,11,'埼玉県'),
(3,12,'千葉県'),
(3,13,'東京都'),
(3,14,'神奈川県'),
(4,15,'新潟県'),
(4,16,'富山県'),
(4,17,'石川県'),
(4,18,'福井県'),
(4,19,'山梨県'),
(4,20,'長野県'),
(4,21,'岐阜県'),
(4,22,'静岡県'),
(4,23,'愛知県'),
(5,24,'三重県'),
(5,25,'滋賀県'),
(5,26,'京都府'),
(5,27,'大阪府'),
(5,28,'兵庫県'),
(5,29,'奈良県'),
(5,30,'和歌山県'),
(6,31,'鳥取県'),
(6,32,'島根県'),
(6,33,'岡山県'),
(6,34,'広島県'),
(6,35,'山口県'),
(7,36,'徳島県'),
(7,37,'香川県'),
(7,38,'愛媛県'),
(7,39,'高知県'),
(8,40,'福岡県'),
(8,41,'佐賀県'),
(8,42,'長崎県'),
(8,43,'熊本県'),
(8,44,'大分県'),
(8,45,'宮崎県'),
(8,46,'鹿児島県'),
(8,47,'沖縄県')
;

drop table if exists java_type;
create table java_type (
	char_type char(10) default 'char'
	,varchar_type varchar(10) default 'varchar'
	,tinytext_type tinytext
	,text_type text
	,mediumtext_type mediumtext
	,longtext_type longtext
	,tinyint_type tinyint default 1
	,smallint_type smallint default 1
	,mediumint_type mediumint default 1
	,int_type int default 1
	,bigint_type bigint default 1
	,float_type float default 1.1
	,double_type double default 1.1
	,date_type date default '2001-01-01'
	,datetime_type datetime default '2001-01-01 00:00:00'
	,timestamp_type timestamp default '2001-01-01 00:00:00'
	,time_stype time default '00:00:00'
	,year_type year default '2001'
	,binary_type binary
	,varbinary_type varbinary(10)
	,tinyblob_type tinyblob
	,blob_type blob
	,mediumblob_type mediumblob
	,longblog_type longblob
	,enum_type enum('red','blue','yellow') default 'red'
	,set_type set('green', 'orange', 'pink') default 'green'
	,geometry_type geometry
	,point_type point
	,linestring_type linestring
	,polygon_type polygon
	,multipoint_type multipoint
	,multilinestring_type multilinestring
	,multipolygon_type multipolygon
	,geometrycollection_type geometrycollection
) engine=innodb charset=utf8mb4 row_format=dynamic comment='java型確認マスタ';
