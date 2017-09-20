import com.akkademy.SClient
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by chandlerzhao on 2017/9/20.
  */
class SClientIntegrationSpec extends FunSpecLike with Matchers {

  val client = new SClient("127.0.0.1:2551")

  describe("akkademyDbClient") {
    it("should set a value") {
      client.set("123", new Integer(123))
      val futureResult = client.get("123")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)
    }


  }

}
