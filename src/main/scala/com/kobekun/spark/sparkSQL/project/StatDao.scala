package com.kobekun.spark.sparkSQL.project

import java.sql.{Connection, PreparedStatement, SQLException}

import scala.collection.mutable.ListBuffer

/**
  * 批量保存DayVideoAccessTopNStat到数据库
  */
object StatDao {

  def insertDayVideoAccessTopNStat(list: ListBuffer[DayVideoAccessStat]): Unit ={

    var conn: Connection = null
    var pstmt: PreparedStatement = null

    try{

      conn = MysqlUtils.getConnection()

      //自动提交改成手工提交
      conn.setAutoCommit(false)

      val sql = "insert into  day_video_access_topn_stat(day,cms_id,times) values(?,?,?)"
      pstmt = conn.prepareStatement(sql)

      for(ele <- list){
        pstmt.setString(1,ele.day)
        pstmt.setLong(2,ele.cmsId)
        pstmt.setLong(3,ele.times)

        pstmt.addBatch()
      }

      pstmt.executeBatch() // 执行批量处理
      conn.commit() // 手动提交

    }catch {
      case e: SQLException => e.printStackTrace()
    }finally {
      MysqlUtils.releaseResource(conn,pstmt)
    }
  }



}
