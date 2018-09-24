# Sagittarius

*General-purpose task library for Apache Ant*


## Introduction

**Sagittarius** is a simple, self-contained library of tasks ready to be plugged into [Apache Ant](https://ant.apache.org/); consequently, it can also be referenced by other build tools having an Ant subsystem - such as *Maven* or *Gradle*.


## Requirements

This library is designed for *Java 7* or later compatible version; it also requires a reasonably recent version of Apache Ant and - for some tasks - of Apache Ivy as well.


## Installation

To install Sagittarius, you need to:

1. Ensure that both Ant and Ivy are correctly installed

1. Clone the GitHub project to a local directory

1. Within the project directory, run:

   ```bash
   ant install
   ```

1. After that, you can freely delete the project directory


## Referencing the library

To reference the task library within an Ant project, the easiest way is to install it (see above), then change your project's **build.xml** as follows:

```xml
<project name="MyProject" xmlns:ivy="antlib:org.apache.ivy.ant" xmlns:sagittarius="antlib:info.gianlucacosta.sagittarius" ...>

...

</project>
```

It is interesting to note that the **ivy** namespace is often required as Sagittarius contains tasks having the **ivy:resolve** task as a prerequisite for their own execution.


## Tasks

### ivyRelease

Performs an Ivy-based release of the project, *using its current Ivy revision*. It can be especially useful when adopting Continuous Integration systems such as Travis CI or Jenkins.

**Ivy's resolution process is a prerequisite of this task**.

This task's attributes are:

* **ivyDescriptorPath**: the path of the **ivy.xml** descriptor (default: *ivy.xml*)

* **remote**: the reference Git remote (default: *origin*)

* **branch**: the current Git branch (default: *master*)

* **redownloadDependenciesTargets**: comma-separated list of targets to be executed to download the project dependencies - to ensure that all the dependencies correctly reference existing libraries before performing the release. (default: *no targets*)


The algorithm it applies is as follows:

1. Ensure that every single dependency either has no **branch** attribute or has its value NOT containing the **HEAD** substring (case insensitive)

1. If **redownloadDependenciesTargets** is set, execute such targets

1. Setup Ivy's **info** tag in **ivy.xml**:

   * If the **branch** attribute exists, set its value to the same value as **revision**

   * Set the **status** attribute to **release**

1. Commit the changes using a predefined message and push it to **remote**



### ivyPostRelease

Performs Ivy-based post-release activities; it is designed to be called after the **ivyRelease** task described above.

**Ivy's resolution process is a prerequisite of this task**.

This task's attributes are:

* **ivyDescriptorPath**: the path of the **ivy.xml** descriptor (default: *ivy.xml*)

* **remote**: the Git remote (default: *origin*)

* **branch**: the current Git branch (default: *master*)

* **artifactUrlTemplate**: the HTTP URL that should be polled in order to ensure that the artifact has been correctly deployed - by the build system or even a CI server (default: *no value*, therefore triggering no polling). It can include a few placeholders:

  * **%ORGANISATION%**: Ivy's **organisation** attribute for this module

  * **%MODULE%**: Ivy's **module** attribute for this module

  * **%REVISION%**: Ivy's **revision** attribute for this module

* **artifactUrlUsername**: the username required by the artifact HTTP server (default: *no username*, therefore performing no authentication)
* **artifactUrlPassword**: the password required by the artifact HTTP server

* **artifactUrlMaxPolls**: the number of queries to be performed to the artifact server (default: *10*)

* **artifactUrlRetryWaitInMillis**: the interval, in milliseconds, between subsequent queries to the artifact server (default: *20000*)

* **tagTemplate**: the template string to be used to generate Git's tag. It can include **%REVISION%**, to inject Ivy's **revision** attribute for the module (default: *v%REVISION%*)


The task applies the following algorithm:

1. If **artifactUrlTemplate** is set, generate a URL by injecting Ivy's module coordinates, then poll until an HTTP request returns 200-OK or all the retries fail (which would stop the build process)

1. Create a Git tag using **tagTemplate** and push it to **remote**

1. Update **ivy.xml**:

   * Increase by 1 the rightmost version component of Ivy's **revision**

   * If **branch** is defined, set it to **HEAD**

   * Set **status** to **integration**

1. Commit using a standard message and push to **origin**



## Further references

* [Introduction to Apache Ivy](https://speakerdeck.com/giancosta86/introduction-to-apache-ivy) - my presentation dedicated to Apache Ivy