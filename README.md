# inbound-ra-example
Example project for an inbound resource adapter on Wildfly 10 application server

** In progress - many open questions **

## Introduction
In this project I want to build a inbound resource adapter, because the documentation
of an inbound resource adapter to an external eis system is very rare, outdated and incomplete.

The first question is, wth. is a resource adapter? In short, a resource adapter is an adapter
to connect an external enterprise information system (EIS), that is not part of the wildfly application server.
This can be, for example a mailserver or an external webservice.

There are three types of resource adapters:

- **inbound** for incoming information from an EIS
- **outbound** for outgoing information to an EIS
- **bidirectional** for both directions in one resource adapter

The resource adapter framework, used by wildfly 10 is ironjacamar 1.3.4.Final. The 
[Ironjacamar](http://www.ironjacamar.org/) project site provide an outdated version 1.2.7 and is hosted 
on sourceforge. After a short research, I found the current version project on gihub, but the documentation are
the same like in 1.2.7. A clearly documentation or examples for an inbound recource adapter are missing. All examples describe
an outbound resource adapter.

The Ironjacamar framework has a sourcegenerator to create all three types of source generator. I use maven as buildsystem
and these codegenerator was my main research focus. The first thing I'm wondering about was the packagin of the generated 
resource adapter, every generated resource adapter was packaged as jar and not as rar. A rar archive is a packaged 
resource adapter and not a compressed rar archive.

I will use the rar packaging format in this example.

- **tcp-ra** the imbound recource adapter example. These jar is needed to build the tcp-eis resource adapter archive
- **tcp-eis** the as rar packaged inbound resource adapter 
- **ra-user-ejb** an example ejb container that use a mdb to receive and answer messages from the eix system
- **ra-user-web** a primefaces test web archive to test messages from the resource adapter as serversided events. (not ready now)
- **ra-ear** a enterprise archive that contain the ejb and the web container

## Build and use it
Start the wildfly 10.1.0 application server and go in the top level folder and
execute `mvn install`. The next step is to deploy the resource adapter. Go in the
tcp-eis folder and execute
`mvn -X clean install rar:rar wildfly:deploy`. It should run without problems.

Execute in the home folder of the wildfly 10.1.0 application server
`bin/jboss-cli.sh`. Execute the following commands:

```
connect
/subsystem=resource-adapters/resource-adapter=tcp-eis.rar:add(archive=tcp-eis.rar,transaction-support=NoTransaction)
/subsystem=resource-adapters/resource-adapter=tcp-eis.rar:read-resource(recursive=true)
```

The last command is to check the settings. If the resource adapter is deployed
succesful, it can be connected with following command `telnet 127.0.0.1 10345` 

```
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
test123
broadcasting to mdb's test123
an other message
broadcasting to mdb's an other message
hello world
broadcasting to mdb's hello world
quit
Connection closed by foreign host.
```
The next step is the deployment of the ear file with the `InboundEventHandler` MDB. 
These MDB consume the messages from the inbound resource adapter. Go in the project
folder `ra-ear` and execute `mvn wildfly:deploy`. The deployment process start
and the following error is shown an the console:

```
2017-10-06 14:39:35,351 WARN  [org.jboss.modules] (MSC service thread 1-7) Failed to define class de.bitc.ejb.InboundEventHandler in Module "deployment.ra-ear.ear.ra-user-ejb-0.0.1-SNAPSHOT.jar:main" from Service Module Loader: java.lang.NoClassDefFoundError: Failed to link de/bitc/ejb/InboundEventHandler (Module "deployment.ra-ear.ear.ra-user-ejb-0.0.1-SNAPSHOT.jar:main" from Service Module Loader): de/bitc/jca/inflow/TcpMessageListener
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
        at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:446)
        at org.jboss.modules.ModuleClassLoader.loadClassLocal(ModuleClassLoader.java:274)
        at org.jboss.modules.ModuleClassLoader$1.loadClassLocal(ModuleClassLoader.java:78)
        at org.jboss.modules.Module.loadModuleClass(Module.java:606)
        at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:190)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:363)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClass(ConcurrentClassLoader.java:351)
        at org.jboss.modules.ConcurrentClassLoader.loadClass(ConcurrentClassLoader.java:93)
        at java.lang.Class.forName0(Native Method)
        at java.lang.Class.forName(Class.java:348)
        at org.jboss.as.ee.utils.ClassLoadingUtils.loadClass(ClassLoadingUtils.java:21)
        at org.jboss.as.ee.utils.ClassLoadingUtils.loadClass(ClassLoadingUtils.java:14)
        at org.jboss.as.ee.component.deployers.InterceptorAnnotationProcessor.processComponentConfig(InterceptorAnnotationProcessor.java:84)
        at org.jboss.as.ee.component.deployers.InterceptorAnnotationProcessor.deploy(InterceptorAnnotationProcessor.java:76)
        at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:147)
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1948)
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1881)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

2017-10-06 14:39:35,351 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-7) MSC000001: Failed to start service jboss.deployment.subunit."ra-ear.ear"."ra-user-ejb-0.0.1-SNAPSHOT.jar".POST_MODULE: org.jboss.msc.service.StartException in service jboss.deployment.subunit."ra-ear.ear"."ra-user-ejb-0.0.1-SNAPSHOT.jar".POST_MODULE: WFLYSRV0153: Failed to process phase POST_MODULE of subdeployment "ra-user-ejb-0.0.1-SNAPSHOT.jar" of deployment "ra-ear.ear"
        at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:154)
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1948)
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1881)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)
Caused by: java.lang.NoClassDefFoundError: Failed to link de/bitc/ejb/InboundEventHandler (Module "deployment.ra-ear.ear.ra-user-ejb-0.0.1-SNAPSHOT.jar:main" from Service Module Loader): de/bitc/jca/inflow/TcpMessageListener
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
        at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:446)
        at org.jboss.modules.ModuleClassLoader.loadClassLocal(ModuleClassLoader.java:274)
        at org.jboss.modules.ModuleClassLoader$1.loadClassLocal(ModuleClassLoader.java:78)
        at org.jboss.modules.Module.loadModuleClass(Module.java:606)
        at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:190)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:363)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClass(ConcurrentClassLoader.java:351)
        at org.jboss.modules.ConcurrentClassLoader.loadClass(ConcurrentClassLoader.java:93)
        at java.lang.Class.forName0(Native Method)
        at java.lang.Class.forName(Class.java:348)
        at org.jboss.as.ee.utils.ClassLoadingUtils.loadClass(ClassLoadingUtils.java:21)
        at org.jboss.as.ee.utils.ClassLoadingUtils.loadClass(ClassLoadingUtils.java:14)
        at org.jboss.as.ee.component.deployers.InterceptorAnnotationProcessor.processComponentConfig(InterceptorAnnotationProcessor.java:84)
        at org.jboss.as.ee.component.deployers.InterceptorAnnotationProcessor.deploy(InterceptorAnnotationProcessor.java:76)
        at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:147)
        ... 5 more

2017-10-06 14:39:35,364 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 17) WFLYCTL0013: Operation ("add") failed - address: ([("deployment" => "ra-ear.ear")]) - failure description: {
    "WFLYCTL0080: Failed services" => {"jboss.deployment.subunit.\"ra-ear.ear\".\"ra-user-ejb-0.0.1-SNAPSHOT.jar\".POST_MODULE" => "org.jboss.msc.service.StartException in service jboss.deployment.subunit.\"ra-ear.ear\".\"ra-user-ejb-0.0.1-SNAPSHOT.jar\".POST_MODULE: WFLYSRV0153: Failed to process phase POST_MODULE of subdeployment \"ra-user-ejb-0.0.1-SNAPSHOT.jar\" of deployment \"ra-ear.ear\"
    Caused by: java.lang.NoClassDefFoundError: Failed to link de/bitc/ejb/InboundEventHandler (Module \"deployment.ra-ear.ear.ra-user-ejb-0.0.1-SNAPSHOT.jar:main\" from Service Module Loader): de/bitc/jca/inflow/TcpMessageListener"},
    "WFLYCTL0412: Required services that are not installed:" => ["jboss.deployment.subunit.\"ra-ear.ear\".\"ra-user-ejb-0.0.1-SNAPSHOT.jar\".POST_MODULE"],
    "WFLYCTL0180: Services with missing/unavailable dependencies" => undefined

```

The main problem is
**ailed to link de/bitc/ejb/InboundEventHandler (Module "deployment.ra-ear.ear.ra-user-ejb-0.0.1-SNAPSHOT.jar:main" from Service Module Loader): de/bitc/jca/inflow/TcpMessageListener**
and I found no solution.

## How the example are created


At first I generated the resource adapter with:

```
doc/codegenerator (git)-[ironjacamar-1.3.5.Final] % ./codegenerator.sh 
Profile version (1.7/1.6/1.5/1.0) [1.7]: 
Type (O/Outbound/I/Inbound/B/Bidirectional) [O]: I
Package name: de.bitc.jca
Use annotations (Y/Yes/N/No) [Y]: 
Resource adapter class name [AcmeResourceAdapter]: TcpResourceAdapter
Should the resource adapter class be Serializable (Y/Yes/N/No) [Y]: Y
Resource adapter config properties [enter to quit]: 
    Name: port
    Type: Integer
    Value: 10345 

Resource adapter config properties [enter to quit]: 
    Name: 
MessageListener interface name [TcpMessageListener]: 
ActivationSpec class name [TcpActivationSpec]: 
ActivationSpec config properties [enter to quit]: 
    Name: topic
    Type: String
    Value: topic1
    Required (Y/Yes/N/No) [N]: Y

ActivationSpec config properties [enter to quit]: 
    Name: 
Activation class name [TcpActivation]: 
Include an admin object (Y/Yes/N/No) [N]: 
Use JBoss Logging  (Y/Yes/N/No) [N]: 
Build environment [A/Ant/I/Ant+Ivy/M/Maven/G/Gradle] [A]: M
Code generated
```
I renamed it to tcp-ra, this is the core of the resource adapter. It will be packaged in an additional rar project.

Next I created a sample ejb project the use with a message driven bean (mdb) the resource adapter. The mdb will receive messages that sended with the
resource adapter. I use the fast prototyping tool jboss forge in the jboss tools ide.

In jboss forge:

```
[inbound-ra-example]$ project-new --named ra-user-ejb --stack JAVA_EE_7 --build-system Maven
***SUCCESS*** Project named 'ra-user-ejb' has been created.
***SUCCESS*** Stack 'Java EE 7' installed in project
[ra-user-ejb]$ ejb-setup
***SUCCESS*** EJB has been installed.
[ra-user-ejb]$ jms-setup
***SUCCESS*** JMS has been installed.
```





Open questions are:
- what is the task of an admin object? I found no clear description about it. Oracle did not describe it in detail and in the jacamar project I found also nothing.
- The Ironjacamar codegenerator ask me to use annotations. Why I need also the ra.xml
resource adpater discriptor? It is a configuration hell.

References:
- [Thinking in Java EE (at least trying to!)](https://abhirockzz.wordpress.com/2015/01/19/mdb-jms-and-vice-versa/)
- [Prozesse und Systeme - very good guide, but in german](http://www.prozesse-und-systeme.de/jcaEinleitung.html)
- [Resource Adapters and Contracts - Java EE 7](https://docs.oracle.com/javaee/7/tutorial/resources.htm#BNCJH)
- [MDB Listener for inbound JCA adapter doesn't start in WildFly](https://stackoverflow.com/questions/35625685/mdb-listener-for-inbound-jca-adapter-doesnt-start-in-wildfly)

