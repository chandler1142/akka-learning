package com.askdemo

import akka.actor.Actor

/**
  * Created by chandlerzhao on 2017/9/30.
  */
class ParsingActor extends Actor {
  override def receive: Receive = {
    case ParseHtmlArticle(key, html) =>
      sender() ! ArticleBody(key, de.l3s.boilerpipe.extractors.ArticleExtractor.INSTANCE.getText(html))
    case x =>
      println("unknown message " + x.getClass)
  }
}
