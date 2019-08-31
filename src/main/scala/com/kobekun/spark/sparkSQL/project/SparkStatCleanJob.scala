package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.SparkSession

/**
  * 数据清洗--日志解析
  */
object SparkStatCleanJob {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("SparkStatFormatJob")
      .master("local[2]").getOrCreate()

    //RDD
    val access = spark.sparkContext.textFile("F:\\BaiduNetdiskDownload\\11.11 log\\access.log")

    //RDD => DF
    val accessDF = spark.createDataFrame(access.map(line => AccessLogConvertUtil.parse(line))
      ,AccessLogConvertUtil.struct)

    accessDF.printSchema()
    accessDF.show(false)

    spark.stop()
  }

}
