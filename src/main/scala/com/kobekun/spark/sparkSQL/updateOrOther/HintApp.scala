package com.kobekun.spark.sparkSQL.updateOrOther

import java.util.UUID

import org.apache.spark.sql.SparkSession

object HintApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("HintApp")
      .master("local[2]")
      .getOrCreate()

    val users = 1.to(1000).map(x =>{
      User(x, UUID.randomUUID().toString, x%100)
    })

    import spark.implicits._
    val userDF = spark.sparkContext.parallelize(users).toDF


    userDF.createOrReplaceTempView("users")

//    和hive有关的在spark-shell中执行
    //查看执行计划
//    /* REPARTITION(3) */ 在spark2.4里面才生效
    spark.sql("create table kobekun_users as select /* REPARTITION(3) */ age,count(1)" +
      " from users where age between 10 and 60 group by age")
        .explain()





//    Interface through which the user may create, drop, alter or query underlying
//       databases, tables, functions etc.
    val catalog = spark.catalog

    catalog.listDatabases().show(false)

    catalog.listDatabases().select("name").show(false)

    //default为默认数据库
    catalog.listTables("default").show(false)
    //函数
    catalog.listFunctions().show(200,false)

    spark.stop()

  }

  case class User(id:Int, name: String, age: Int)
}
