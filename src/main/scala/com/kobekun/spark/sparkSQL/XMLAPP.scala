package com.kobekun.spark.sparkSQL

import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.sql.types.{ArrayType, LongType, StringType, StructField, StructType}

object XMLAPP {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("XMLAPP")
      .master("local[2]").getOrCreate()


//    val innerSchema = StructType(
//      StructField("ItemData",
//        ArrayType(
//          types.StructType(
//            StructField("IdKey",LongType,true)::
//              StructField("Value",LongType,true)::Nil
//          )
//        ),true)::Nil
//    )
//    val schema = StructType(
//      StructField("CDate",StringType,true)::
//        StructField("ListItemData", innerSchema, true):: Nil
//    )

    val path = "E:\\IdeaProjects\\sparkSQLOverture\\src\\main\\resources\\complexXml.xml"

    //需要指定每个字段，并使nullable为true，字段可为空值，如果使用spark自动推导schema信息，某些
    //https://blog.csdn.net/zpf336/article/details/88827081
    val innerSchema = StructType(
      Array(
        StructField("ItemData",
          ArrayType(
            StructType(
              Array(
                StructField("IdKey",LongType,true),
                StructField("Value",LongType,true)
              )
            ),true
          ),true
        )
      )
    )

    val schema = StructType(
      StructField("CDate",StringType,true) ::
        StructField("ListItemData",innerSchema,true) :: Nil
    )

    import spark.implicits._

    val xmlDF = spark.read.format("com.databricks.spark.xml")
      .option("rowTag","Item")
      //    问题： 如果省去shcema会有什么问题？
      //    由于spark会根据xml文件自动推断schema，如果xml文件局部节点不完整，不会有问题，
      //    如果全部文件都少掉了一个节点，那么推断出来的shcema将得不到你想要的完整的schema
      .schema(schema)
      .load(path)
      .withColumn("ItemData",functions.explode($"ListItemData.ItemData"))
      .select("CDate","ItemData.*")

    xmlDF.show()

//      +--------------------+-----+-----+
//      |               CDate|IdKey|Value|
//      +--------------------+-----+-----+
//      |2018-05-08T00:00::00|    2|    1|
//      |2018-05-08T00:00::00|   61|    2|
//      +--------------------+-----+-----+

    spark.stop()
  }
}


//* {{{
//*   case class Book(title: String, words: String)
//*   val ds: Dataset[Book]
//*
//*   val allWords = ds.select('title, explode(split('words, " ")).as("word"))
//*
//*   val bookCountPerWord = allWords.groupBy("word").agg(countDistinct("title"))
//* }}}
//*
//* Using `flatMap()` this can similarly be exploded as:
//*
//* {{{
//*   ds.flatMap(_.words.split(" "))
//* }}}
//*
//* @group untypedrel
//* @since 2.0.0
//*/
//@deprecated("use flatMap() or select() with functions.explode() instead", "2.0.0")
//def explode[A <: Product : TypeTag](input: Column*)(f: Row => TraversableOnce[A]): DataFrame = {


//Caused by: java.lang.ClassNotFoundException: org.apache.spark.sql.types.DataType
//报这个错的原因是：
//原因：自己在pom文件中加入
//<scope>provided</scope>
//此标签表示编译环境可用，运行时则不可用，主要是在 打包时不将这些依赖打进来，因为服务器上有，以免包过大

