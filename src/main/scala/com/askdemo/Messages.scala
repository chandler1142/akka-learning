package com.askdemo

/**
  * Created by chandlerzhao on 2017/9/30.
  */


case class ParseArticle(url: String)
case class ParseHtmlArticle(url: String, htmlString: String)
case class HttpResponse(body: String)
case class ArticleBody(url: String, body: String)


