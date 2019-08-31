package com.kobekun.spark.sparkSQL.project

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import org.apache.commons.lang3.time.FastDateFormat

/**
  * SimpleDateFormat是线程不安全的
  */
object DateUtils {

//  val INPUT_DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH)
  val INPUT_DATE_FORMAT = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z",Locale.ENGLISH)

  val OUTPUT_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

  /**
    * 获取目标格式时间字符串
    */
  def parse(time: String) ={
      OUTPUT_DATE_FORMAT.format(new Date(getTime(time)))
  }

  /**
    * 获取时间的long类型
    * @param time
    * @return
    */
  def getTime(time: String) ={

    try{
      INPUT_DATE_FORMAT.parse(time.substring(time.indexOf("[")+1,time.indexOf("]"))).getTime
    }catch {
      case e: Exception => 0l
    }
  }
}
