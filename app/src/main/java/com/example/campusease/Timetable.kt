package com.example.campusease

data class Timetable(
    val course_name:String?=null,
    val course_teacher:String?=null,
    val DayOne:DayClass?=null,
    val DayTwo:DayClass?=null
):java.io.Serializable
