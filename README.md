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

- **tcp-ra** the imbound recource adapter example
- **ra-user-ejb** an example ejb container that use a mdb to receive and answer messages from the eix system
- **tcp-eis** the as rar packaged inbound resource adapter 

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


References:
- [Thinking in Java EE (at least trying to!)](https://abhirockzz.wordpress.com/2015/01/19/mdb-jms-and-vice-versa/)
- [Prozesse und Systeme - very good guide, but in german](http://www.prozesse-und-systeme.de/jcaEinleitung.html)
- [Resource Adapters and Contracts - Java EE 7](https://docs.oracle.com/javaee/7/tutorial/resources.htm#BNCJH)

