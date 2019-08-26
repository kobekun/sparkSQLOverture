package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * DataFrame和RDD 互操作
  */
object DataFrameRDD {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDD")
      .master("local[2]").getOrCreate()

    //inferReflection(spark)

    program(spark)

    spark.stop()
  }

  private def inferReflection(spark: SparkSession) = {
    // RDD ==> DataFrame
    val RDD = spark.sparkContext.textFile("file:\\C:\\Users\\mouse\\Desktop\\infos.txt")

    //需要导入隐式转换
    import spark.implicits._
    val infoDF = RDD.map(_.split(",")).map(line => Info(line(0).toInt, line(1), line(2).toInt))
      .toDF()

    infoDF.show()

    infoDF.filter(infoDF.col("age") > 30).show()

    infoDF.createOrReplaceTempView("infos")

    spark.sql("select * from infos where age > 30").show
  }

  private def program(spark: SparkSession): Unit ={

    val RDD = spark.sparkContext.textFile("file:\\C:\\Users\\mouse\\Desktop\\infos.txt")

    val infoRDD = RDD.map(_.split(',')).map(line => Row(line(0).toInt,line(1),line(2).toInt))

    val structType = StructType(Array(StructField("id",IntegerType,true),

      //如果name被声明为int类型，抛出下面的异常
      // Caused by: java.lang.RuntimeException: java.lang.String is not a valid external type for schema of int
      StructField("name",StringType,true),
      StructField("age",IntegerType,true)))

    val infoDF = spark.createDataFrame(infoRDD,structType)

    infoDF.printSchema()
    infoDF.show()

    infoDF.filter(infoDF.col("age") > 30).show()

    infoDF.createOrReplaceTempView("infos")

    spark.sql("select * from infos where age > 30").show

  }

  case class Info(id: Int, name: String, age: Int)
}
