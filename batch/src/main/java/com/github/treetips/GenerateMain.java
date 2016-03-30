package com.github.treetips;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.ClassUtils;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Target;
import org.jooq.util.mysql.MySQLDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.treetips.domain.model.tables.Area;
import com.github.treetips.domain.model.tables.Prefecture;
import com.github.treetips.domain.model.tables.records.PrefectureRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GenerateMain {

	@Autowired
	private DSLContext dsl;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private DataSourceProperties dataSourceProperties;

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(GenerateMain.class);
		try {
			GenerateMain main = ctx.getBean(GenerateMain.class);
			main.flywayTest();
			main.jooqGenTest();
			main.jooqSqlTest();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		SpringApplication.exit(ctx);
	}

	private void flywayTest() {
		log.info("############################################################################");
		log.info("# Flywary migration.");
		log.info("############################################################################");
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setBaselineOnMigrate(true);
		flyway.migrate();
	}

	private void jooqGenTest() {
		log.info("############################################################################");
		log.info("# jOOQ generation.");
		log.info("############################################################################");
		String generatorClassName = ClassUtils.getPackageName(MySQLDatabase.class) + "."
				+ ClassUtils.getShortClassName(MySQLDatabase.class);
		try {
			Configuration configuration = new Configuration().withJdbc(new Jdbc() //
					.withDriver(dataSourceProperties.getDriverClassName()) //
					.withUrl(dataSourceProperties.getUrl()) //
					.withUser(dataSourceProperties.getUsername()) //
					.withPassword(dataSourceProperties.getPassword()) //
			).withGenerator(new Generator().withDatabase(new Database() //
					.withName(generatorClassName) //
					.withIncludes(".*") //
					.withExcludes("(batch_.*|schema_version)") // flywayとspring-batch管理テーブルを除外
					.withInputSchema(dataSourceProperties.getSchema()) //
			).withTarget(new Target() //
					.withPackageName("com.github.treetips.domain.model") //
					.withDirectory("../base/src/main/java")));
			GenerationTool.generate(configuration);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void jooqSqlTest() {
		log.info("############################################################################");
		log.info("# jOOQ SQL.");
		log.info("############################################################################");
		{
			System.out.println("==========================================================================");
			System.out.println("= where句テスト");
			System.out.println("==========================================================================");
			List<PrefectureRecord> results = dsl.selectFrom(Prefecture.PREFECTURE)
					.where(Prefecture.PREFECTURE.PREFECTURE_CD.eq(Byte.parseByte("13")))
					.fetchInto(PrefectureRecord.class);
			results.forEach(System.out::println);
		}
		{
			System.out.println("==========================================================================");
			System.out.println("= selectするカラムを絞るテスト");
			System.out.println("==========================================================================");
			List<PrefectureRecord> results = dsl.select(Prefecture.PREFECTURE.PREFECTURE_NAME)
					.from(Prefecture.PREFECTURE).where(Prefecture.PREFECTURE.PREFECTURE_CD.eq(Byte.parseByte("13")))
					.fetchInto(PrefectureRecord.class);
			results.forEach(System.out::println);
		}
		{
			System.out.println("==========================================================================");
			System.out.println("= group by + count()テスト");
			System.out.println("==========================================================================");
			List<Record2<Byte, Integer>> results = dsl.select(Prefecture.PREFECTURE.AREA_CD, DSL.count())
					.from(Prefecture.PREFECTURE).groupBy(Prefecture.PREFECTURE.AREA_CD).fetch();
			for (Record2<Byte, Integer> record2 : results) {
				System.out.println(record2);
			}
		}
		{
			System.out.println("==========================================================================");
			System.out.println("= 1対1 joinテスト");
			System.out.println("==========================================================================");
			Byte[] prefectureCds = { 11, 12, 13 };
			List<Record> results = dsl
					.selectFrom(Prefecture.PREFECTURE.join(Area.AREA)
							.on(Prefecture.PREFECTURE.AREA_CD.equal(Area.AREA.AREA_CD)))
					.where(Prefecture.PREFECTURE.PREFECTURE_CD.in(prefectureCds)).fetch();
			results.forEach(System.out::println);
		}
		{
			System.out.println("==========================================================================");
			System.out.println("= 1対n joinテスト");
			System.out.println("==========================================================================");
			Byte[] areaCds = { 1, 2 };
			List<Record> results = dsl
					.selectFrom(Area.AREA.join(Prefecture.PREFECTURE)
							.on(Area.AREA.AREA_CD.equal(Prefecture.PREFECTURE.AREA_CD)))
					.where(Area.AREA.AREA_CD.in(areaCds)).fetch();
			results.forEach(System.out::println);
		}
	}
}
