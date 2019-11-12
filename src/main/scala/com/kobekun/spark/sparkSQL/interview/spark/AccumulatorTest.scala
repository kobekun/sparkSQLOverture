package com.kobekun.spark.sparkSQL.interview.spark

import org.apache.spark.sql.SparkSession

object AccumulatorTest {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[2]")
      .appName("AccumulatorTest").getOrCreate()
    val sc = spark.sparkContext

    val accum = sc.longAccumulator("longAccum") //统计奇数的个数

    val sum = sc.parallelize(Array(1,2,3,4,5,6,7,8,9),2).filter(n=>{
      if(n%2!=0) accum.add(1L)
      n%2==0
    }).reduce(_+_)

    println("sum: "+sum)
    println("accum: "+accum.value)
  }
}
