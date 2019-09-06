package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 数据清洗--日志解析
  */
object SparkStatCleanJobYarn {


  def main(args: Array[String]): Unit = {

    if(args.length != 2){
      println("Usage: SparkStatCleanJobYarn <inputPath> <outputPath>")
      System.exit(1)
    }
    val Array(inputPath,outputPath) = args

    val spark = SparkSession.builder()
//      .appName("SparkStatFormatJob")
//      .master("local[2]")
      .getOrCreate()

    //RDD
    val access = spark.sparkContext.textFile(inputPath)

    //RDD => DF
    val accessDF = spark.createDataFrame(access.map(line => AccessLogConvertUtil.parse(line))
      ,AccessLogConvertUtil.struct)

//    accessDF.printSchema()
//    accessDF.show(false)

    //以一个分区数将数据写入到一个文件 coalesce，以重写的形式
    //调优点，控制文件输出的个数或者大小：coalesce   ****
    accessDF.coalesce(1).write.format("parquet")
      .mode(SaveMode.Overwrite)
      .partitionBy("day")
      .save(outputPath)

    spark.stop()
  }

}
