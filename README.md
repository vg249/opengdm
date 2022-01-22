# Project dependencies:

## Setup instructions:

> 1) Import the project at the parent pom level into your ide;
> 
> 2) Run Maven install and import the maven types into your IDE;
> 
> 3) The Maven compile configuration is set to Java 1.8, so you should have the JDK 1.8
>    installed on your system, and configure your IDE to use JDK 1.8 for gobiiproject.
> 
> 3) Set up a tomcat 7.0 maven run configuration in your IDE:
>     * Use conference of the conf directory in the gobiiproject-conf.zip in this
>       project for the conf directory of the tomcat instance;
>     * Deploy the war produced by the gobii-web project;
>     * When deployed and running, the following REST call should produce a
>       meaningful result:
>       curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"name\":\"article\",\"scope\":\"dumb scope\"}" http://localhost:8181/resourceParam/search/bycontenttype
> 
> 4) Set up a run configuration for org/gobiiproject/gobiiprocess/ExtractorProcess.java;
>    when you run it, you should see that the service call it uses logs a meaningful result.
> 
> 5) Set up a maven run configuration in the dao project as follows:
> 
>    Commandline: clean hibernate3:hbm2hbmxml hibernate3:hbm2java compile
>    Working directory: <physical-path-to-module> (e.g., C:/phil-source/IntelliJ/gobiiproject/gobii-dao)
> 
>    In order for this to work, you also have to modify resources/hibernate.properties in order
>    connect to the database. Upon completion, this run task will create jpa/hibernate entity
>    classes from the database tables.
> 
>    After you run the task, you will also have to do Generate Sources/Update Folders in order
>    for the classes to be seen by the IDE compiler.



