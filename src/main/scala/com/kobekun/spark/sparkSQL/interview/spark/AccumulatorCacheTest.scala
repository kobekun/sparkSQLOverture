package com.kobekun.spark.sparkSQL.interview.spark

import org.apache.spark.sql.SparkSession

object AccumulatorCacheTest {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[2]")
      .appName("AccumulatorCacheTest").getOrCreate()
    val sc = spark.sparkContext

    val accum = sc.accumulator(0,"Error Accumulator")

    val data = sc.parallelize(1 to 10)

    val newdata = data.map{x => {
      if(x%2 == 0) {
        accum +=1
        0
      }else 1
    }}

//    //使用action触发执行
//    newdata.count()
//    // 5
//    println(accum.value)
//
//    //查看刚才变动的数据，foreach也是action操作
//    newdata.foreach(println)
//
//    //查看累加器，值进行了map操作  变为 10
//    println(accum.value)

//    避免上面累加器值变的方案有：1、操作一次action 2、将数据存到cache或persist中
//    切断任务之间的依赖关系

    //使用cache切除依赖
    newdata.cache().count()
    //此时accum的值为5
    println(accum.value)

    newdata.foreach(println)
    //此时累加器值依然是 5
    println(accum.value)
  }
}
