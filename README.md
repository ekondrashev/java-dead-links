# Important considerations
* Attention to requirement details counts
* No commented code

# Step 2
Having a PR from `Step1` for the `master` branch, rework the code in a way so the client code from [`Main.java`](https://github.com/ekondrashev/java-dead-links/blob/step2/src/main/java/Main.java) compiles and the output still produces result making test green:
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
* Test calling `main(String[] args)` and verifying the program system output correctness. Several options to consider:
  * Make use of `Before/After` junit annotations to replace/restore `System.out` `Writer`, see [SO](https://stackoverflow.com/a/1119559) answer for details
  * See http://stefanbirkner.github.io/system-rules/ rules helping to gather system output. See `System.err and System.out` section in particular

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
