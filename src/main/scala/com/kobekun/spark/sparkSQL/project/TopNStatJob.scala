package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.expressions.Window
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
    //按照天统计视频的TopN访问量
//    videoAccessTopNStat(spark,accessDF)
    //按照地市统计视频的TopN访问量
//    cityAccessTopNStat(spark,accessDF)
    //按照流量进行统计
    trafficAccessTopNStat(spark,accessDF)
    spark.stop()
  }

  def trafficAccessTopNStat(spark: SparkSession, accessDF: DataFrame) = {

    import  spark.implicits._

    val trafficAccessTopNDS = accessDF.filter($"day" === "20170511" && $"cmsType" === "video")
      .groupBy("day","cmsId")
      .agg(sum("traffic").as("traffics"))
      .orderBy("traffics")

        trafficAccessTopNDS.printSchema()
        trafficAccessTopNDS.show(false)

    try{

      trafficAccessTopNDS.foreachPartition(partitionOfRecord =>{

        val list = new ListBuffer[DayTrafficVideoAccessStat]

        partitionOfRecord.foreach(info => {

          val day =  info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val traffics = info.getAs[Long]("traffics")

          list.append(DayTrafficVideoAccessStat(day,cmsId,traffics))
        })

        StatDao.insertDayTrafficVideoAccessTopNStat(list)
      })
    }catch {
      case e: Exception => e.printStackTrace()
    }
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


  def cityAccessTopNStat(spark: SparkSession, accessDF: DataFrame) = {

    /**
      * 按照dataframe的方式进行统计
      */
    import  spark.implicits._

    val cityAccessTopNDS = accessDF.filter($"day" === "20170511" && $"cmsType" === "video")
      .groupBy("day","city","cmsId")
      .agg(count("cmsId").as("times"))

//    cityAccessTopNDS.printSchema()
//    cityAccessTopNDS.show(false)

    //Window函数在sparkSQL中的应用
    val cityVideoAccessStatDF = cityAccessTopNDS.select(
      cityAccessTopNDS("day"),
      cityAccessTopNDS("city"),
      cityAccessTopNDS("cmsId"),
      cityAccessTopNDS("times"),
      row_number().over(Window.partitionBy(cityAccessTopNDS("city"))
        .orderBy(cityAccessTopNDS("times").desc))
        .as("timesRank")
    ).filter("timesRank <= 3")  //top3
//          .show(false)

    try{

      cityVideoAccessStatDF.foreachPartition(partitionOfRecord =>{

        val list = new ListBuffer[DayCityVideoAccessStat]

        partitionOfRecord.foreach(info => {

          val day =  info.getAs[String]("day")
          val city = info.getAs[String]("city")
          val cmsId = info.getAs[Long]("cmsId")
          val times = info.getAs[Long]("times")
          val timesRank = info.getAs[Int]("timesRank")

          list.append(DayCityVideoAccessStat(day,city,cmsId,times,timesRank))
        })

        StatDao.insertDayCityVideoAccessTopNStat(list)
      })
    }catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
