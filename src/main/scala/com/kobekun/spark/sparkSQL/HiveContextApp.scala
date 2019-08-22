package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * hiveContext的使用
  * 使用时需要通过 --jars 把mysql的驱动传递到参数中
  */
object HiveContextApp {

  def main(args: Array[String]): Unit = {

//    1) 创建context
    val sconf = new SparkConf()
    //在测试或者生产环境中，建议将下面信息注释，下面是通过脚本进行指定
//    sconf.setAppName("HiveContextApp").setMaster("local[2]")

    val sc = new SparkContext(sconf)
    val hiveContext = new HiveContext(sc)

//    2) 相关处理

    hiveContext.table("emp").show()
//    3) 关闭资源
    sc.stop()
  }
}
