package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer

object TopNStatJob {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("TopNStatJob")
      .config("spark.sql.sources.partitionColumnTypeInference.enabled","false")
      .master("local[2]").getOrCreate()

    val accessDF = spark.read.format("parquet")
      .load("F:\\BaiduNetdiskDownload\\clean")

//    accessDF.printSchema()
//    accessDF.show(false)
    videoAccessTopNStat(spark,accessDF)


    spark.stop()
  }

  def videoAccessTopNStat(spark: SparkSession, accessDF: DataFrame) = {

    /**
      * 按照dataframe的方式进行统计
      */
    //    import  spark.implicits._
    //
    //    val videoAccessTopNDS = accessDF.filter($"day" === "20170511" && $"cmsType" === "video")
    //      .groupBy("day","cmsId")
    //      .agg(count("cmsId").as("times"))
    //      .orderBy($"times".desc)
    //
    //    videoAccessTopNDS.show(false)

    /**
      * 按照sparkSQL的方式进行统计
      */
    accessDF.createOrReplaceTempView("access_log")

    val videoAccessTopNDF = spark.sql("select day,cmsId,count(1) as times from access_log" +
      " where day='20170511' and cmstype='video'" +
      " group by day,cmsId" +
      " order by times desc")

    videoAccessTopNDF.show(false)

    /**
      * 将统计结果写入到mysql数据库
      *
      */
    try{

      videoAccessTopNDF.foreachPartition(partitionOfRecord =>{

        val list = new ListBuffer[DayVideoAccessStat]

        partitionOfRecord.foreach(info => {

          val day =  info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val times = info.getAs[Long]("times")

          list.append(DayVideoAccessStat(day,cmsId,times))
        })

        StatDao.insertDayVideoAccessTopNStat(list)
      })
    }catch {
      case e: Exception => e.printStackTrace()
    }

  }
}
