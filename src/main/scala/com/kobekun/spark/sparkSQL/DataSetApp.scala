package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

object DataSetApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataSetApp")
      .master("local[2]").getOrCreate()

    val path = "file:\\C:\\Users\\mouse\\Desktop\\sales.csv"

    import  spark.implicits._
    //spark 解析CSV文件
    val df = spark.read.option("header","true")
      .option("inferSchema","true")
      .csv(path)

    val ds = df.as[Sales]
    ds.map(line => line.amountPaid).show()


    spark.stop()
  }

  case class Sales(transactionId: Int,customerId: Int,itemId: Int,amountPaid: Double)
}
