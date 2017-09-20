package com.akkademy.messages

/**
  * Created by chandlerzhao on 2017/8/30.
  */
case class SetRequest(key: String, value: Object)
case class GetRequest(key: String)
case class KeyNotFoundException(key: String) extends RuntimeException