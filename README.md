# gitbucket-monitoring-plugin

[![](https://travis-ci.org/YoshinoriN/gitbucket-monitoring-plugin.svg?branch=master)](https://travis-ci.org/YoshinoriN/gitbucket-monitoring-plugin) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/677a69ed2e5f4137ba35986dbb02995f)](https://www.codacy.com/app/YoshinoriN/gitbucket-monitoring-plugin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=YoshinoriN/gitbucket-monitoring-plugin&amp;utm_campaign=Badge_Grade)

# Features

This plugin provides display machine information, resources, processes, Java information and GitBucket's log for [GitBucket](//github.com/gitbucket/gitbucket) Administrator.

All viewable informations are below.

* Operating System Information (OS, Distribution, Version, Architecture)
* Uptime (When booted, Spent time after booted)
* Time (Now time, Time Zone, Zone offset, Day of Week)
* Machine's enviroment valiables
* Machine's resources (Cpu core, Cpu Usaga, Physical memory, Swap, Disk Usaga)
* Machine's Load average
* Machine's Tasks (Running, Sleepling, Stopped, Zombie)
* Java system properties and memory
* LogBack settings
* GitBucket's log

# Images

||||
|:-------:|:-------:|:-------:|
|![menu](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/menu.png)|![system](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/system.png)|![envval](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/envval.png)|
|![resources](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master//doc/images/resources.png)|![process](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/process.png)|![java-p](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/java-p.png)|
|![java-m](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/java-m.png)|![logback](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/logback.png)|![gitbucketlog](https://raw.githubusercontent.com/YoshinoriN/gitbucket-monitoring-plugin/master/doc/images/gitbucketlog.png)|

# Download & Installation

1. Download plugin jar file from [the release page](//github.com/YoshinoriN/gitbucket-monitoring-plugin/releases).
2. Put plugin jar file into `GITBUCKET_HOME/plugins` and restart GitBucket.

# UI Usage

Goto the `System Administration` menu, you can see `Monitoring` section.

# Compatibility with GitBucket

|Plugin version|GitBucket version|
|:-------------:|:-------:|
|1.1.0|4.10 - 4.19.x|
|1.0.0|4.10 - 4.19.x|

# Supported OS

I confirm work it only `Debian GNU/Linux 8`, `Ubuntu 16.04` and `Windows10`.  But I think maybe also will be work it on other distribution(version) of Linux and Window. Mac is also.

# Build from source

```sh
sbt package
```

The built package will be created at `/target/scala-2.12/gitbucket-monitorting-plugin_2.12-{plugin-version}.jar`

# License

This project is under the Apache License, Version 2.0 License. See the [LICENSE](./LICENSE) file for the full license text.
