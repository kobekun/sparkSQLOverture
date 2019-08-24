package com.kobekun.spark.sparkSQL

import java.sql.DriverManager

object SparkSQLThriftServer {

  def main(args: Array[String]): Unit = {

    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val conn = DriverManager.getConnection("jdbc:hive2://hadoop001:14000","hadoop","")

    //select empno,ename,sal from emp;  ==> select empno,ename,sal from emp
    val pstmt = conn.prepareStatement("select empno,ename,sal from emp")

    val rs = pstmt.executeQuery();

    while(rs.next()){

      println("empno: " + rs.getInt("empno") + " ,ename: " +
        rs.getString("ename") + " , sal: " +
        rs.getString("sal"));
    }

    rs.close
    pstmt.close
    conn.close
  }
}
