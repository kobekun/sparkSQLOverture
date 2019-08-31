package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.SparkSession

object SparkStatFormatJob {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("SparkStatFormatJob")
      .master("local[2]").getOrCreate()

    val access = spark.sparkContext.textFile("F:\\BaiduNetdiskDownload\\11.11 log\\access.20161111.log.gz")

//    access.take(10).foreach(println)

    access.map(line =>{

      val splits = line.split(" ")

      val ip = splits(0)
      val time = splits(3) + " " + splits(4)
      val parseTime = DateUtils.parse(time)
      val traffic = splits(9)
      val url = splits(11)

      parseTime + "\t" + url + "\t" + traffic + "\t" + ip
      //      (ip,parseTime)
    }).saveAsTextFile("F:\\BaiduNetdiskDownload\\11.11 log\\output")
//      .take(10).foreach(println)
    spark.stop()
  }
}
