package com.example.campusease

data class studentModel(
    var cms:String?=null,
    var name:String?=null,
    var email:String?=null,
    var department:String?=null,
    var semester:String?=null,
    var section:String?=null,
    var approved:Boolean=false,
    var password: String?=null,
    var courses:List<String>?=null
    ):java.io.Serializable