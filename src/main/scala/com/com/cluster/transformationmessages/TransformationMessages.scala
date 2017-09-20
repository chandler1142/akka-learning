package com.com.cluster.transformationmessages

/**
  * Created by chandlerzhao on 2017/9/21.
  */

case class TransformationJob(text: String)

case class TransformationResult(text: String)

case class JobFailed(reason: String, job: TransformationJob)


object TransformationMessages {
  val BACKEND_REGISTRATION = "BackendRegistration"
}