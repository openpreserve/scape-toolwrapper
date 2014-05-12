# Contribute to ToolWrapper

*Wrap preservation tools once, deploy them everywhere.*

## Develop

[![Build Status](https://travis-ci.org/openplanets/scape-toolwrapper.png)](https://travis-ci.org/openplanets/scape-toolwrapper)

### Requirements

To build you require:

 * Maven 3: http://maven.apache.org/
 * clone this repo (if you haven't)

For using the recommended IDE you require:

 * Eclipse: http://www.eclipse.org/downloads/index-developer.php

### Setup IDE

After you install eclipse and clone the repo, install the 
plugins listed above. To install a plugin click on Help > Eclipse Market Place
and search them or just use the nice drag and drop feature and drag them from the links above.

As soon as you are ready import the maven modules by selecting File > Import > Maven > Existing Maven Projects.
Maven will fetch the whole internet (this is normal) and will import the projects for you.


### Build

To compile go to the sources folder and execute the command:

```bash
$ mvn clean install
```

### Deploy

After building the necessary jars will be available on the target directories and the binaries on the `bin/` directories will be able to run them.

## Contribute

1. [Fork the GitHub project](https://help.github.com/articles/fork-a-repo)
2. Change the code and push into the forked project
3. [Submit a pull request](https://help.github.com/articles/using-pull-requests)

To increase the chances of your changes being accepted and merged into the official source here's a checklist of things to go over before submitting a contribution. For example:

* Has unit tests (that covers at least 80% of the code)
* Has documentation (at least 80% of public API)
* Agrees to contributor license agreement, certifying that any contributed code is original work and that the copyright is turned over to the project

## Roadmap

TBA
