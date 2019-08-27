package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

/**
  * parquet文件操作
  */
object ParquetApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("ParquetApp")
      .master("local[2]").getOrCreate()

    val path = "file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet"

    val jsonPath = "file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json"

    val userDF = spark.read.format("parquet").load(path)

    spark.read.format("parquet").option("path","file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet").load().show
    userDF.printSchema()
    userDF.show()

    userDF.select("name","favorite_color").show

    userDF.select("name","favorite_color").write.format("json").save("/home/hadoop/tmp/output/json")

    //spark默认加载parquet格式的文件
    spark.read.load(path).show

    //如果是json文件会抛出以下错误
    //java.lang.RuntimeException: file:/home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0
    // /examples/src/main/resources/people.json is not a Parquet file
    spark.read.load(jsonPath).show
    spark.stop()
  }
}
