package com.kobekun.spark.sparkSQL.project

//地市统计的视频访问 实体类
case class DayCityVideoAccessStat(day: String, city: String,
                                  cmsId: Long, times: Long, timesRank: Int)
