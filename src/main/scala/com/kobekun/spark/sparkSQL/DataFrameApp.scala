package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

object DataFrameApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp")
      .master("local[2]").getOrCreate()

    // 返回DataFrame
    val peopleDF = spark.read.format("json").load("file:\\C:\\Users\\mouse\\Desktop\\people.json")

    //输出schema信息
    peopleDF.printSchema()

    //输出前20条记录
    peopleDF.show

    //查询某个列的信息
    peopleDF.select("name").show

    //查询某几列的信息 年龄加10  select name, (age+10) as age2 from people
    peopleDF.select(peopleDF.col("name"),(peopleDF.col("age")+10).as("age2")).show

    //filter select * from people where age >19;
    peopleDF.filter(peopleDF.col("age")>19).show


    //根据某一列进行分组，然后再进行聚合操作 select age,count(1) from people group by age
    peopleDF.groupBy("age").count().show

    spark.stop()
  }
}
