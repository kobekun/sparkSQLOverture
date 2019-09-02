package com.kobekun.spark.sparkSQL.project

//按流量统计的视频访问实体类
case class DayTrafficVideoAccessStat(day: String, cmsId: Long, traffics: Long)