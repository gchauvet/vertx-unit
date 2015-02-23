package examples;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Examples {

  public static void test_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case", test -> {
      String s = "value";
      test.assertEquals("value", s);
    });
    suite.run();
  }

  public static void test_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case", test -> {
      String s = "value";
      test.assertEquals("value", s);
    });
    suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
  }

  public static void writing_test_suite_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case_1", test -> {
      // Test 1
    });
    suite.test("my_test_case_2", test -> {
      // Test 2
    });
    suite.test("my_test_case_3", test -> {
      // Test 3
    });
  }

  public static void writing_test_suite_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case_1", test -> {
      // Test 1
    }).test("my_test_case_2", test -> {
      // Test 2
    }).test("my_test_case_3", test -> {
      // Test 3
    });
  }

  public static void writing_test_suite_03() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.before(test -> {
      // Test suite setup
    }).test("my_test_case_1", test -> {
      // Test 1
    }).test("my_test_case_2", test -> {
      // Test 2
    }).test("my_test_case_3", test -> {
      // Test 3
    }).after(test -> {
      // Test suite cleanup
    });
  }

  public static void writing_test_suite_04() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.beforeEach(test -> {
      // Test case setup
    }).test("my_test_case_1", test -> {
      // Test 1
    }).test("my_test_case_2", test -> {
      // Test 2
    }).test("my_test_case_3", test -> {
      // Test 3
    }).afterEach(test -> {
      // Test case cleanup
    });
  }

  public static void asserting_01(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", test -> {
      test.assertEquals(10, callbackCount);
    });
  }

  public static void asserting_02(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", test -> {
      test.assertEquals(10, callbackCount, "Should have been 10 instead of " + callbackCount);
    });
  }

  public static void asserting_03(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", test -> {
      test.assertNotEquals(10, callbackCount);
    });
  }

  public static void asserting_04(io.vertx.ext.unit.TestSuite suite, boolean var, int value) {
    suite.test("my_test_case", test -> {
      test.assertTrue(var);
      test.assertFalse(value > 10);
    });
  }

  public static void asserting_05(io.vertx.ext.unit.TestSuite suite) {
    suite.test("my_test_case", test -> {
      test.fail("That should never happen");
      // Following statements won't be executed
    });
  }

  public static void async_01(io.vertx.ext.unit.TestSuite suite, EventBus eventBus) {
    suite.test("my_test_case", test -> {
      Async async = test.async();
      eventBus.consumer("the-address", msg -> {
        // <2>
        async.complete();
      });
      // <1>
    });
  }

  public static void async_02(io.vertx.ext.unit.TestSuite suite, Vertx vertx) {
    suite.test("my_test_case", test -> {

      Async async1 = test.async();
      HttpClient client = vertx.createHttpClient();
      HttpClientRequest req = client.get(8080, "localhost", "/");
      req.exceptionHandler(err -> test.fail(err.getMessage()));
      req.handler(resp -> {
        test.assertEquals(200, resp.statusCode());
        async1.complete();
      });
      req.end();

      Async async2 = test.async();
      vertx.eventBus().consumer("the-address", msg -> {
        async2.complete();
      });
    });
  }

  public static void async_03(io.vertx.ext.unit.TestSuite suite, Vertx vertx, Handler<HttpServerRequest> requestHandler) {
    suite.before(test -> {
      Async async = test.async();
      HttpServer server = vertx.createHttpServer();
      server.requestHandler(requestHandler);
      server.listen(8080, ar -> {
        test.assertTrue(ar.succeeded());
        async.complete();
      });
    });
  }
}
