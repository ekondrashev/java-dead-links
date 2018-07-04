# Important considerations
* Attention to requirement details counts
* No commented code

# Step 5. Testing mode introduction

Using PR from the step below, rework the code so the program recognizes 
`--enable-http-recording=http_recording.json` cmdline option, enabling recording of all the HTTP responses into specified 
file for testing purposes(`http_recording.json` will be used in further steps, like enable mocking).

## Recording file example

```json
{ "url_1": {
  "code": 404,
  "asString": "html"
    }
}
```

Please note that 
* `asString` value should be considered optional, since string representation of HTTP response is not 
always needed
* if the recording json file exists, it should be replaced with a new one
* test should still be green


# ~~Step 4~~

Basing on PR from previous step, rework the code, introducing `HTTP.Default` implementation 
that `Links.HTML` should expect as second argument. 
Try moving existing code related to the transport aspects to that implementation. The test should still be green.

# ~~Step 3~~
Basing on PR from `Step 2`, rework the code in a way to meet following requirements
* Move expected JSON(YML/XML) from test sources out to `src/test/resources` reading the it with `Class.getResource*`
* Study [Typical Mistakes in Java Code](https://www.yegor256.com/2014/04/27/typical-mistakes-in-java-code.html) and make sure that at least the test case method namings follow [described](https://www.yegor256.com/2014/04/27/typical-mistakes-in-java-code.html#test-method-names) convention.


# ~~Step 2~~
Having a PR from `Step 1` for the `master` branch, rework the code in a way so the client code from [`Main.java`](https://github.com/ekondrashev/java-dead-links/blob/step2/src/main/java/Main.java) compiles and the output still produces result making test green:
```
class Main {
    public static void main(String[] args) {
        Links html = new Links.HTML(args[0]);
        System.out.println(html.toString());
    }
}
```

# ~~Step 1~~
Please suggest a PR with a
* Java code implementing the below contract;
* Test calling `main(String[] args)` verifying the sys output correctness, several options to consider:
  * Make use of `Before/After` junit annotations to replace/restore `System.out` `Writer`, see [SO](https://stackoverflow.com/a/1119559) answer for details
  * See http://stefanbirkner.github.io/system-rules/ rules helping catching system output, `System.err and System.out` section in particular


## Input arguments
The program expects target url to take HTML(and check for dead links) from.

## Output format
The program execution result should be written to `System.out`. Here is example output:

```
{
    "url": "<input_url_tocheck>"
    "404":  {
        "size": 3,
	"urls": ["url1", "url2", "url3"]
    },
    "50x": {
        "size": 1,
	"urls" ["url4", ]
    }
    "dead": 4,
    "total": 10
}
```

* It is ok to use YML/XML or any other format considering the test verifying this format is available.
