package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

object DataFrameJustDo {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDD")
      .master("local[2]").getOrCreate()

    val studentRDD = spark.sparkContext
      .textFile("file:\\C:\\Users\\mouse\\Desktop\\student.data")

    //需要导入隐式转换
    import spark.implicits._
    val student = studentRDD.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2),line(3)))
      .toDF()

    student.show()
    student.show(30,false)

    //下面三个方法没有show方法
    //返回值 Array[Row]
    student.take(10)
    //返回值 Row
    student.first()
    //返回值 Array[Row]
    student.head(3)

//    student.select("email").show(30,false)
//
//    student.filter("name=''").show()
//    student.filter("name='' or name='NULL'").show

    //name以M开头的人
//    student.filter("SUBSTR(name,0,1) = 'M'").show()
//
//    student.sort(student("name")).show()
//    student.sort("name").show
//    student.sort(student("name").desc).show
//
//    student.sort("name","id").show
//    student.sort(student("name").asc, student("id").desc).show
//
//    student.select(student("name").as("student_name")).show
//
//    val student2 = studentRDD.map(_.split("\\|"))
//      .map(line => Student(line(0).toInt, line(1), line(2),line(3)))
//      .toDF()
//
//    student.join(student2,student.col("id") === student2.col("id")).show

    spark.stop
  }
}
case class Student(id: Int, name: String, phone: String, email: String)
