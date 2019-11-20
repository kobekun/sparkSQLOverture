package com.kobekun.spark.sparkSQL.interview.spark

import org.apache.spark.{SparkConf, SparkContext}

object ReduceByKeyAndWindow {


  val myfunc = (it: Iterator[(String, Seq[Int], Option[Int])]) => {
    it.map(x => {
      (x._1, x._2.sum + x._3.getOrElse(0))
    })
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("window")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(2))
    sc.setCheckpointDir("C:\\Users\\Administrator\\Desktop\\myck01")
    val ds = ssc.socketTextStream("192.168.80.123", 9999)
    //Seconds(5)表示窗口的宽度   Seconds(3)表示多久滑动一次(滑动的时间长度)
    val re = ds.flatMap(_.split(" ")).map((_, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(20), Seconds(10))
    //    窗口长度和滑动的长度一致,那么类似于每次计算自己批量的数据,用updateStateByKey也可以累计计算单词的wordcount 这里只是做个是实验
    //    val re = ds.flatMap(_.split(" ")).map((_, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(4), Seconds(4)).updateStateByKey(myfunc, new HashPartitioner(sc.defaultParallelism), true)
    re.print()
    ssc.start()
    ssc.awaitTermination()
  }
  //https://blog.csdn.net/qq_20641565/article/details/76906686
}
