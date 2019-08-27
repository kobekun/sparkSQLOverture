package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

object HiveMySQL {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("ParquetApp")
      .master("local[2]").getOrCreate()

    val hiveDF = spark.table("emp")

    val mysqlDF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306/hive").option("dbtable",
      "spark.DEPT").option("user", "root").option("password", "root").option("driver","com.mysql.jdbc.Driver").load()

    //join
    val resultDF = hiveDF.join(mysqlDF,hiveDF.col("deptno") === mysqlDF.col("DEPTNO"))

    resultDF.show

    resultDF.select(hiveDF.col("ename"),hiveDF.col("empno"),mysqlDF.col("deptno"),mysqlDF.col("dname")).show

    spark.stop
  }
}
