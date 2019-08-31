package com.kobekun.spark.sparkSQL.project

import com.ggstar.util.ip.IpHelper

object IPUtils {

  def getCity(ip: String) = {
    IpHelper.findRegionByIp(ip)
  }

  def main(args: Array[String]): Unit = {

    println(getCity("58.30.15.255"))
  }
}
