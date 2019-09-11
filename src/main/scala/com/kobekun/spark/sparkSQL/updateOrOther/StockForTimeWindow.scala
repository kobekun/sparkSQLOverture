package com.kobekun.spark.sparkSQL.updateOrOther

import org.apache.spark.sql.SparkSession

//股票 窗口函数的使用
object StockForTimeWindow {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("StockForTimeWindow")
      .master("local[2]")
      .getOrCreate()

    val stockDF = spark.read.format("csv")
      .option("header","true")
      .option("inferSchema","true")
      .load("E:\\IdeaProjects\\sparkSQLOverture\\src\\main\\resources\\xomtable.csv")

    //时间推导出来为 timestamp
    stockDF.printSchema()
    stockDF.show(false)


    /**
      * 统计2016年股票周平均收盘价格
      *
      * 1、2016 ：Date
      * 2、收盘价格: Close
      * 3、平均：agg(avg(""))
      * 4、周  TimeWindow
      */

    val stock2016DF = stockDF.filter("year(Date) == 2016")
//    stock2016DF.show(false)

    import org.apache.spark.sql.functions._   //window
    /**
      * 窗口
      * 第一个字段：时间所在列
      * 第二个字段：窗口周期duration
      */
    stock2016DF.groupBy(window(stock2016DF.col("Date"), "1 week"))
        .agg(avg("Close").as("week_close_average"))
        .sort("window.start")
        .select("window.start","window.end","week_close_average")
        .show(false)

    spark.stop()
  }
}
