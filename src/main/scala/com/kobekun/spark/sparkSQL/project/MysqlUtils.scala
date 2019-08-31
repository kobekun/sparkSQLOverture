package com.kobekun.spark.sparkSQL.project

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

/**
  * create table day_video_access_topn_stat(
  *   day varchar(10) not null,
  *   cms_id bigint(10) not null,
  *   times bigint(10) not null,
  *   primary key(day,cms_id)
  * ) ;
  */
object MysqlUtils {

  val url = "jdbc:mysql://localhost:3306/imooc_sparksql?useSSL=false"
  val username = "root"
  val password = "881105"

  def getConnection() = {

//    Class.forName("com.jdbc.mysql.Driver")
    DriverManager.getConnection(url,username,password)
  }

  /**
    * 释放数据库资源
    * @param conn
    * @param pstmt
    * @param rs
    */
  def releaseResource(conn: Connection, pstmt: PreparedStatement, rs: ResultSet) = {
    try{
      if(pstmt != null){
        pstmt.close()
      }
      if(rs != null){
        rs.close()
      }

    }catch {
      case e: Exception => e.printStackTrace()

    }finally {
      if(conn != null){
        conn.close()
      }
    }

  }

  def main(args: Array[String]): Unit = {

    println(getConnection())
  }
}

//mysql抛出下面异常
//WARN: Establishing SSL connection without server's identity verification is not recommended.
//According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default
//if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'.
//You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
//Exception in thread "main" java.sql.SQLException: Unknown system variable 'query_cache_size'

//解决方法：
//jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false



//Unknown system variable 'query_cache_size'

//解决方案：pom.xml文件中mysql版本升高到和本机安装的版本一致

//create table day_video_access_topn_stat(
//  day varchar(10) not null,
//  cms_id bigint(10) not null,
//  times bigint(10) not null,
//  primary key(day,cms_id)
//) ;