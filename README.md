# web-crawler

This web-crawler is incorporated into a sping boot web app. 

There is an executable jar called ```web-crawler.jar```.

To build, enter the command:

```gradle makeWebCrawler```

This creates and executable jar in build/libs.

To execute the web-crawler, enter the command:

``` java -jar ./build/libs/web-crawler.jar```

Incorporated into the web-crawler is Swagger. To see the available endpoints, enter the
following into your web browser:

```localhost:8888/swagger-ui.html```

The endpoint to use is ```getSiteMap``` with a parameter of the main url, such as ```http://www.google.com```

Please include the http or https protocol in the url string.

The return object is a set of endpoint pages.

