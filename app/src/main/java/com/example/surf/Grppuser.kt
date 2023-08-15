package com.example.surf

class Grppuser {
    var gcode:String?=null
    var role:String?=null
    constructor(){
    }
    constructor(role:String?,gcode:String?,){
        this.gcode= gcode
        this.role=role
    }
}