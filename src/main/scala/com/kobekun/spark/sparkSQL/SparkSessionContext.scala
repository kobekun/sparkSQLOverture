package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

/**
  * sparksession使用
  */

object SparkSessionContext {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("SparkSessionContext")
      .master("local[2]").getOrCreate()

    val people = spark.read.json("file:\\C:\\Users\\mouse\\Desktop\\people.json")
    people.show()

    spark.stop()
  }
}
