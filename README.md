# TSMock

## Introduction
tsmock(Test service - Mock) is a self-contain full function mock server writen by pure Java, it has rich functions to support complex mock scenarios
## Key functions
1. Support mulitple platforms: Windows, Linux and any platform jre supported
2. Support multiple protocols: http, ali metaq
3. Support sync and async mock
4. Support multiple type callbacks: http, ali metaq, websocket
5. Support compressed(gzip)
6. Support http proxy mode(forward to next node)
7. Support request validation
8. Support data process, transform
9. Easy to extend function by beanshell script

## Prerequest
Jre 8 or higher
## Installation
It's a self-contain jar file, just put it in a folder and use it, no installation need
## Usage
1. Start
    * `java -jar tsmock.jar -h <http configuration file,json format> -m <metaq configuration file,json format> -l <log path>`
    * `nohup java -jar tsmock.jar -h <http configuration file,json format> -m <metaq configuration file,json format> -l <log path> >/dev/null 2>&1 &`
2. Stop
    * Just kill it by `ps -ef|grep xxx|awk '{print $2}'|xargs kill -15`
3. Configuration
    * Please refer to http_sample.json and mq_sample.json in the project config folder
4. Log
    * TSMock will generate 2 type logs under the log path you specify in the cmdl
        * tsmock.log: execution log include all the info,warning,error during running.
        * trans.log: just request and response pair for the infomation tsmock receive and send back  
        * logBody parameter in config file turn on or off the request/response body recording  
        
## Ongoing feature
1. UI
2. More protocols 

## Author
Jun Qin at Alibaba  

## Bug and issues
If you find any problems, be free to send mail to qinjun.qj@alibaba-inc.com
## License
MIT © Jun Qin
