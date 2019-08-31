package com.kobekun.spark.sparkSQL.project

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object AccessLogConvertUtil {

  val struct = StructType(
    Array(
      StructField("url",StringType),
      StructField("cmsType",StringType),
      StructField("cmsId",LongType),
      StructField("traffic",LongType),
      StructField("ip",StringType),
      StructField("city",StringType),
      StructField("time",StringType),
      StructField("day",StringType)
    )
  )

  /**
    * 根据输入的每一行信息转换成输出的样式
    * @param log  输入的每一行记录
    */
  def parse(log: String) = {

    try{
      val splits = log.split("\t")

      val url = splits(1)
      val domain = "http://www.imooc.com/"
      val cms = url.substring(url.indexOf(domain)+domain.length).split("/")
      var cmsType = ""
      var cmsId = 0l
      if(cms.length > 1){
        cmsType = cms(0)
        cmsId = cms(1).toLong
      }


      val traffic = splits(2).toLong
      val ip = splits(3)

      val city = ""
      val time = splits(0)
      val day = time.substring(0,10).replaceAll("-","")

      //    StructField("url", StringType)
      //    StructField("cmsType", StringType)
      //    StructField("cmsId", LongType)
      //    StructField("traffic", LongType)
      //    StructField("ip", StringType)
      //    StructField("city", StringType)
      //    StructField("time", StringType)
      //    StructField("day", StringType)

      //row中字段和structtype里面的一致
      Row(url,cmsType,cmsId,traffic,ip,city,time,day)
    }catch {
      case e:Exception => Row(0)
    }

  }

}
