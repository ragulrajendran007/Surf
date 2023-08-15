package com.example.surf




class Post{
    var descp:String?=null
    var senderid:String?=null
    var image:String?=null
    constructor(){}
    constructor(descp: String?,senderid:String?,image:String?){
        this.descp=descp
        this.senderid=senderid
        this.image=image
    }
}