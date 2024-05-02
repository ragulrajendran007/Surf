package com.example.surf

class issueResponse {
    var uid:String?=null
    var role:String?=null
    var iss:String?=null
    var issdes:String?=null
    var puloc:String?=null
    var delloc:String?=null
    var contact:String?=null
    var takenBy:String?=null
    var taken:Int?=null
    var key:String?=null
    var delContact:String?=null
    constructor(){

    }
    constructor(uid:String?,role:String?,iss:String?,issdes:String?,puloc:String?,delloc:String?,contact:String?,takenBy:String?,taken:Int?,key:String?,delContact:String?){

        this.uid=uid
        this.role=role
        this.iss=iss
        this.issdes=issdes
        this.puloc=puloc
        this.delloc=delloc
        this.contact=contact
        this.takenBy=takenBy
        this.taken=taken
        this.key=key
        this.delContact=delContact
    }
}
