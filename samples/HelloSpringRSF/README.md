##### HOW TO RUN THIS SAMPLE?

*  make sure `gradle` is avaiable in your machine
  try `gradle -version`
  otherwise install the `gradle` from http://www.gradle.org/
  

* install spring-rsf's artifacts to your local repository

```
  $ cd spring-rsf
  $ gradle install
```
   *NOTE: spring-rsf will install its artifacts to the Maven Local Repository, which is ~/.m2/repostory in default*

* start web service at 80 port

```
  $ cd smaples
  $ gradle jettyRun
```

* run `ProductServiceTest.testInvokeRpcMethod` test case invoke RPC method.
