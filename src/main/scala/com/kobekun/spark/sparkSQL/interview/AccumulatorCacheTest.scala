package com.kobekun.spark.sparkSQL.interview

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

//spark累加器Accumulator是spark提共的两种共享变量（广播变理和累加器）的一种。为什么要使
//用共享变量呢？通常情况下，当向Spark操作(如map,reduce)传递一个函数时，它会在一个远程集
//群节点上执行，它会使用函数中所有变量的副本。这些变量被复制到所有的机器上，远程机器上并没有
//被更新的变量会向驱动程序回传，也就是说有结果Driver程序是拿不到的！
//共享变量就是为了解决这个问题。解决共享变量，让driver程序拿到变量值，回传给整个程序

//累加器只能够增加。 只有driver能获取到Accumulator的值（使用value方法） ，Task（excutor）
//只能对其做增加操作（使用 +=）。