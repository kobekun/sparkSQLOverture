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


  /**
    * 批量保存DayCityVideoAccessTopNStat到数据库
    */

    def insertDayCityVideoAccessTopNStat(list: ListBuffer[DayCityVideoAccessStat]): Unit ={

      var conn: Connection = null
      var pstmt: PreparedStatement = null

      try{

        conn = MysqlUtils.getConnection()

        //自动提交改成手工提交
        conn.setAutoCommit(false)

        val sql = "insert into  day_city_video_access_topn_stat(day,city,cms_id,times,times_rank) values(?,?,?,?,?)"
        pstmt = conn.prepareStatement(sql)

        for(ele <- list){
          pstmt.setString(1,ele.day)
          pstmt.setString(2,ele.city)
          pstmt.setLong(3,ele.cmsId)
          pstmt.setLong(4,ele.times)
          pstmt.setInt(5,ele.timesRank)

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



  /**
    * 批量保存DayTrafficVideoAccessTopNStat到数据库
    */

  def insertDayTrafficVideoAccessTopNStat(list: ListBuffer[DayTrafficVideoAccessStat]): Unit ={

    var conn: Connection = null
    var pstmt: PreparedStatement = null

    try{

      conn = MysqlUtils.getConnection()

      //自动提交改成手工提交
      conn.setAutoCommit(false)

      val sql = "insert into  day_traffic_video_access_topn_stat(day,cms_id,traffics) values(?,?,?)"
      pstmt = conn.prepareStatement(sql)

      for(ele <- list){
        pstmt.setString(1,ele.day)
        pstmt.setLong(2,ele.cmsId)
        pstmt.setLong(3,ele.traffics)

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

  def deleteData(day: String): Unit ={

    val tables = Array("day_city_video_access_topn_stat"
      ,"day_traffic_video_access_topn_stat"
      ,"day_video_access_topn_stat")

    var conn: Connection = null
    var pstmt: PreparedStatement = null

    try{

      conn = MysqlUtils.getConnection()

      for(table <- tables){

        val deleteSQL = s"delete from $table where day=?"

        pstmt = conn.prepareStatement(deleteSQL)
        pstmt.setString(1,day)

        pstmt.executeUpdate()
      }

    }catch {
      case e: Exception => e.printStackTrace()
    }finally {
      MysqlUtils.releaseResource(conn,pstmt)
    }
  }

}
