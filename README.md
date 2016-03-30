spring-boot-flyway-jooq-example
====

Spring boot + Spring batch + Flyway + Jooqのサンプルプロジェクトです。

## Description

Spring batchを起動し、javaからFlywayでマイグレーションを行い、マイグレーション結果を元にJooq generatorで各モデルクラスを自動生成し、JooqのDSLで各種SQLのテストを行います。javaから任意のタイミングでFlywayとJooq generatorを呼び出しているサンプルとなっています。

MySQLについてはdocker-composeで起動し、起動スクリプトも同梱しています。

### バージョン

- Spring boot version 1.3.3.RELEASE
- Spring batch version 1.3.3.RELEASE
- Flyway version latest
- Jooq version 3.7.3
- MySQL version 5.7
- Docker-engine version latest
- Docker-compose version latest

### プロジェクト構造

```
spring-boot-flyway-jooq-example
　┣ master（gradle関連・docker関連）
　┗ base（親プロジェクト）
　　　┗ batch（バッチプロジェクト）
```

## Requirement

### docker + MySQLサーバ

MySQLサーバが無い場合は、[docker toolbox](https://www.docker.com/products/docker-toolbox)等でdocker環境を構築して下さい。MySQLコンテナの起動等は後述で行うので、docker toolboxのインストールのみ行って下さい。

### IDE

- [Spring Tool Suite](https://spring.io/tools/sts)
- STSにGradle Supportをインストール

## Install

**macの場合はdockerのマウントがホームディレクトリ配下でないと動かない** ので、ホームディレクトリ以下でcloneして下さい。

### Checkout project

```bash
# ホームディレクトリにcd
cd
# checkout
git clone git@github.com:treetips/spring-boot-flyway-jooq-example.git
```

### STS に Gradle Supportをインストール

STS -> Dashboard -> ManageのIDE EXTENSIONS -> Findで「gradle」で検索 -> Gradle Supportをインストール。

### STS でプロジェクトをimmport

STS -> File -> Import -> General -> Existing projects into workspace で

## Usage

### MySQLサーバの起動（MySQLサーバが無い場合）

まず、docker-machineでmysql専用ホストを生成し、起動します。

```bash
docker-machine create -d virtualbox mysql
docker-machine start mysql
```

mysqlホストが起動したら、以下の起動スクリプトでdocker上のMySQLサーバをデーモン起動します。

```bash
cd master/docker/
./start_container.sh
```

続いてjavaを実行します。

/batch/src/main/java/com/github/treetips/GenerateMain.java を実行すると、以下の順に処理が実行されます。

Spring batch -> flyway -> jooq generation -> jooq dsl

### dockerのスクリプト

master/docker にあるファイルは、それぞれ以下の通りです。

<dl>
<dt># connect_mysql.sh</dt><dd>docker上のMySQLサーバにリモート接続します。</dd>
<dt># start_container.sh</dt><dd>MySQLのコンテナをデーモン起動します。</dd>
<dt># stop_container.sh</dt><dd>デーモン起動中のMySQLコンテナを終了します。</dd>
<dt># destroy_container.sh</dt><dd>MySQLコンテナを終了し、破棄します。コンテナが削除されるため、MySQLのデータは全て削除されます。</dd>
<dt># recreate_container.sh</dt><dd>MySQLコンテナを終了し、破棄し、新たにMySQLのコンテナをデーモン起動します。コンテナが削除されるため、MySQLのデータは全て削除されます。</dd>
</dl>

## Licence

[MIT](https://github.com/tcnksm/tool/blob/master/LICENCE)

## Author

[tcnksm](https://github.com/treetips)
