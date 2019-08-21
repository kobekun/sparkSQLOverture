package com.kobekun.spark.sparkSQL

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object SQLContextApp {

  def main(args: Array[String]): Unit = {

    val path = args(0)
//    1) 创建context
    val sconf = new SparkConf()
    //在测试或者生产环境中，建议将下面信息注释，下面是通过脚本进行指定
    sconf.setAppName("SQLContextApp").setMaster("local[2]")

    val sc = new SparkContext(sconf)
    val sqlContext = new SQLContext(sc)

//    2) 相关处理 json
//    val people = sqlContext.read.format("json").load("C:\\Users\\mouse\\Desktop\\people.json")
    val people = sqlContext.read.format("json").load(path)

    people.printSchema()
    people.show()
//    3) 关闭资源
    sc.stop()
  }
}
