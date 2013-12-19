# SCAPE Toolwrapper

The SCAPE Toolwrapper, from now on referred simply as Toolwrapper, is a Java tool developed in the SCAPE Project to simplify the execution of the following tasks:

1. **Tool description** (through the toolspec & optionally with the componentspec);
2. **Tool invocation** (simplified) through command-line wrapping;
3. **Artifacts generation** (associated to a tool invocation, e.g., Taverna workflow);
4. **Packaging** of all the generated artifacts for easier distribution and installation.

**What you'll find in this README:**
* [Toolwrapper and the Tool Spec (toolspec)](#toolwrapper-and-the-tool-spec-toolspec)
* [Toolwrapper and the Component Spec (componentspec)](#toolwrapper-and-the-component-spec-componentspec)
  * [Migration Component](#migration-component)
  * [Characterisation Component](#characterisation-component)
  * [Quality Assurance Component](#quality-assurance-component)
* [Getting started](#getting-started)
  * [Requirements](#requirements)
  * [Project directory structure](#project-directory-structure)
  * [Compilation process](#compilation-process)
  * [How toolwrapper works](#how-toolwrapper-works)
  * [Different Debian package generation scenarios](#different-debian-package-generation-scenarios)
  * [How to generate a Debian package from a toolspec](#how-to-generate-a-debian-package-from-a-toolspec-and-optionally-from-the-respectively-componentspec)
  * [How to develop a specific functionality for the toolwrapper](#how-to-develop-a-specific-functionality-for-the-Toolwrapper)
* [Acknowledgements](#acknowledgements) 

## Toolwrapper and the Tool Spec (toolspec)

Tools, and tools invocations, are described using a machine-readable language (XML, respecting a XML schema) called toolspec. On this file, one can specify:

1. Tool information, i.e., name, version, homepage, etc;
2. Tool installation information, i.e., software dependencies, license, etc;
3. One or more concrete operations, pre-described, that can be executed for a particular input to generate a particular output.

**Example:**

This example, even if simplified for presentation purpose, demonstrates how one could describe a image file format conversion using ImageMagick.

```xml
<?xml version="1.0" encoding="utf-8" ?>
<tool name="ImageMagick" version="2.0.0" 
      homepage="http://www.imagemagick.org/script/convert.php">
   <license name="Apache-2.0" type="FLOSS" 
            uri="http://opensource.org/licenses/Apache-2.0"/>
   <installation>
      <operatingSystem operatingSystemName="Debian">
         <packageManager type="Dpkg">
            <config>imagemagick</config>
            <source>deb http://scape.keep.pt/apt stable main</source>
         </packageManager>
         <dependency name="imagemagick">
            <license name="Apache-2.0" type="FLOSS" 
                     uri="http://opensource.org/licenses/Apache-2.0"/>
         </dependency>
      </operatingSystem>
   </installation>
   <operations>
      <operation name="digital-preservation-migration-image-imagemagick-image2txt">
         <description>
            Converts any ImageMagick supported image format to Text
         </description>
         <command>/usr/bin/convert ${input} txt:${output}</command>
         <inputs>
            <input name="input" required="true">  
               <description>Reference to input file</description>
            </input>
            <parameter name="params" required="false">
               <description>Additional conversion parameters</description>
            </parameter>
         </inputs>
         <outputs>
            <output name="output" required="true">
               <description>Reference to output file</description>
               <extension>txt</extension>               
            </output>
         </outputs>
      </operation>
   </operations>
</tool>

```

## Toolwrapper and the Component Spec (componentspec)

In the SCAPE context, a Component is a Taverna workflow adhering to a Component Profile and used as a building block in a Preservation Action Plan.
These Taverna workflows will "live" in the myExperiment site and allow anyone to search/use them. In order to allow a more meanful search/increase their discoverability, these workflows will be semanticlly annotated with special tool information such as, and for file format migration tools, the supported input formats and output formats, or for the characterisation tools what type of file characteristics the tool can produce, etc.

In what concerns the Toolwrapper, as it produces Taverna workflows from the toolspec and as the toolspec only allows to specify a limited set of information (described in the previous section), another spec file was created and named Component Spec (componentspec).
It is also described using a machine-readable language (XML, respecting a XML schema) and allows one to specify (as specified in the different [SCAPE Component Profiles](https://github.com/openplanets/scape-component-profiles)):

### Migration Component

>One or more migration paths. E.g.
```xml
<migrationPath>
   <fromMimetype>image/png</fromMimetype>
   <toMimetype>text/plain</toMimetype>      
</migrationPath>
```

### Characterisation Component

>One or more accepted mimetypes (the same # as the inputs of the operation). E.g.
```xml
<acceptedMimetype>video/quicktime</acceptedMimetype>
```
>One or more output measures, measures produced by a certain tool operation, which may contain processing instructions that will be added to the Taverna workflow. E.g. (using Bash instruction)
```xml
<outputMeasure name="image_width_of_video" 
               uri="http://purl.org/DP/quality/measures#390"
               typeOfProcessingInstruction="BASH">
egrep "codec_type=\"video\"" %%output%% | sed 's#^.*width="##;s#".*##'
</outputMeasure>
```
or (using JAVA instruction)
```xml
<outputMeasure name="the_height_of_the_video_track" 
               uri="http://purl.org/DP/quality/measures#391"
               typeOfProcessingInstruction="JAVA">
// dummy result (always set the result to 500)
the_height_of_the_video_track="500";
</outputMeasure>
```

### Quality Assurance Component

>Two accepted mimetypes (as this compares 2 representations). E.g.
```xml
<acceptedMimetype>image/*</acceptedMimetype>
<acceptedMimetype>image/*</acceptedMimetype>
```
>One or more output measures, measure that can be "are the two representations equals, using a certain comparison algorithm?". E.g. (using JAVA instruction)
```xml
<outputMeasure name="image_distance_mean_error_squared_MSE" 
               uri="http://purl.org/DP/quality/measures#6" 
               typeOfProcessingInstruction="JAVA">
import java.util.regex.*;
Pattern thePat = Pattern.compile(".+\\((\\d+\\.?\\d*)\\)");
Matcher matcher = thePat.matcher(STDERR_IN);
if (matcher.find()) {
   image_distance_mean_error_squared_MSE = matcher.group(1);
}else{
   image_distance_mean_error_squared_MSE="ERROR";
}
</outputMeasure>
```

**Example:**

This example, even if simplified for presentation purpose, demonstrates how one could describe a image file format conversion using ImageMagick (in what concerns SCAPE Component info).
```xml
<?xml version="1.0" encoding="UTF-8"?>
<components>
   <component xsi:type="MigrationAction" profileVersion="1.0" 
              profile="http://purl.org/DP/components#MigrationAction" 
              name="digital-preservation-migration-image-imagemagick-image2txt" 
              author="Hélder Silva">
      <license name="Apache-2.0" type="FLOSS" 
               uri="http://opensource.org/licenses/Apache-2.0"/>
      <migrationPath>
         <fromMimetype>image/jpeg</fromMimetype>
         <toMimetype>text/plain</toMimetype>      
      </migrationPath>
      <migrationPath>
         <fromMimetype>image/png</fromMimetype>
         <toMimetype>text/plain</toMimetype>      
      </migrationPath>
      <migrationPath>
         <fromMimetype>image/tiff</fromMimetype>
         <toMimetype>text/plain</toMimetype>      
      </migrationPath>
      <migrationPath>
         <fromMimetype>image/jp2</fromMimetype>
         <toMimetype>text/plain</toMimetype>      
      </migrationPath>
   </component>
</components>

```

## Getting started

### Requirements

1. Unix/linux operating system;
2. Java SDK (version >= 1.6)
    * Debian/ubuntu: ```sudo apt-get install openjdk-6-jdk```
3. Build tools (for Java and Debian packaging)
    * Debian/ubuntu: ```sudo apt-get install build-essential dh-make devscripts debhelper lintian maven```
4. Clone of Scape Toolwrapper github repository
    * Unix/linux: ```git clone https://github.com/openplanets/scape-toolwrapper.git```

### Project directory structure

* _**toolwrapper-bash-debian-generator**_ toolwrapper component that generates, from a set of bash wrappers, one or more Debian packages
    * **bin** folder with script that eases the component execution
    * **pom.xml**
    * **src** java source code and other resources (templates for debian package generation)
* _**toolwrapper-bash-generator**_ toolwrapper component that generates a set of bash wrappers and the correspondent Taverna workflows
    * **bin** folder with script that eases the component execution
    * **pom.xml**
    * **src** java source code and other resources (templates for bash wrapper and Taverna workflow)
* **CHANGELOG.txt**
* **toolwrapper-core** toolwrapper component with common core functionalities
    * **pom.xml**
    * **src** java source code and other resources (log4j.xml)
* **toolwrapper-data** toolwrapper component with JAXB related code/resources
    * **pom.xml**
    * **src** java source code and other resources (toolspec & componentspec XML Schema)
* **LICENSE**
* **pom.xml**
* **README_FILES** folder with files mentioned on the README file
* **README.md**

### Compilation process

Execute the following on the command-line ($TOOLWRAPPER\_GITHUB\_FOLDER denotes the path to the folder where the Scape Toolwrapper repository was cloned into):
```bash
$> cd $TOOLWRAPPER_GITHUB_FOLDER
$> mvn package
```

### How toolwrapper works

In the project directory, there are 2 Toolwrapper components (for now) whose name ends up with "generator". These, when executed in a certain sequence, generate different outputs.  
If one executes the **bash-generator** first, for a given toolspec (and optionally for the respectively componentspec), one will end up with a bash wrapper and a Taverna workflow, as the following diagram explains.

<pre>                                             +---------------------+
                                             |  output_directory   |
                                             |---------------------|
                      +----------------+     | ./bash/             |
   +--------+         |                |     |    ./bash_wrapper_1 |
   |toolspec|+------->|                |     |                     |
   +--------+         | bash-generator |+--->| ./workflow/         |
+- - - - - - -+       |                |     |    ./workflow_1     |
|componentspec|+----->|                |     |                     |
+- - - - - - -+       +----------------+     | ./install/          |
                                             |    ./toolspec       |
                                             |    ./componentspec  |
                                             +---------------------+
</pre>
  
Then, if one wants to generate a Debian package, for a given toolspec (and optionally for the respectively componentspec) and for the previously generated artifacts, one executes the **bash-debian-generator**, as the following diagram explains.

<pre> +---------------------+
 |  output_directory   |
 |---------------------|                                     +---------------------+
 | ./bash/             |                                     |  output_directory   |
 |    ./bash_wrapper_1 |+--|                                 |---------------------|
 |                     |   |                                 | ./bash/             |
 | ./workflow/         |   |   +-----------------------+     |    ./bash_wrapper_1 |
 |    ./workflow_1     |   --> |                       |     |                     |
 |                     |       |                       |     | ./workflow/         |
 | ./install/          | ----> | bash-debian-generator |+--->|    ./workflow_1     |
 |    ./toolspec       | |     |                       |     |                     |
 |    ./componentspec  | | --> |                       |     | ./install/          |
 +---------------------+ | |   +-----------------------+     |    ./toolspec       |
        +--------+       | |                                 |    ./componentspec  |
        |toolspec|+------| |                                 |                     |
        +--------+         |                                 | ./debian/           |
     +- - - - - - -+       |                                 |    ./debian_1       |
     |componentspec|+------|                                 +---------------------+
     +- - - - - - -+
</pre>
  
**Sum up:**  
1. Toolwrapper components can be combined, in the correct order, passing generated artifacts through a folder (i.e., the output folder of the **bash-generator** will be the input folder of the **bash-debian-generator**).  
2. An install folder is generated by the **bash-generator**, which can be used to place scripts/files/programs that should be installed alongside with the bash wrapper and workflow. These scripts/files/programs are going to be placed under **/usr/share/OPERATION-NAME/**.
  
### Different Debian package generation scenarios

1. **1 toolspec with 1 operation**  
This will generate 1 Debian package named OPERATION-NAME\_VERSION\_all.deb
2. **1 toolspec with n operations (n > 1)**
    1. Generate a Debian package with all artifacts (named DEB-NAME\_VERSION\_all.deb, where DEB-NAME is passed as a parameter through the command-line)
    2. Generate a Debian package per operation (named OPERATION-NAME\_VERSION\_all.deb) **DEFAULT**

### How to generate a Debian package from a toolspec (and optionally from the respectively componentspec)

Files required:

* toolspec (e.g. digital-preservation-migration-image-imagemagick-image2txt.xml)
* changelog (e.g. digital-preservation-migration-image-imagemagick-image2txt.changelog)

Optional file:

* componentspec (e.g. digital-preservation-migration-image-imagemagick.image2txt.component)

Execute the following on the command-line ($TOOLWRAPPER\_GITHUB\_FOLDER denotes the path to the folder where the Scape Toolwrapper repository was cloned into):

```bash
$> cd $TOOLWRAPPER_GITHUB_FOLDER
$> ./toolwrapper-bash-generator/bin/generate.sh -e hsilva@keep.pt -o output_dir -t \
  README_FILES/digital-preservation-migration-image-imagemagick-image2txt.xml -c \
  README_FILES/digital-preservation-migration-image-imagemagick-image2txt.component 
$> ./toolwrapper-bash-debian-generator/bin/generate.sh -t \
  README_FILES/digital-preservation-migration-image-imagemagick-image2txt.xml -ch \
  README_FILES/digital-preservation-migration-image-imagemagick-image2txt.changelog \
  -i output_dir -o output_dir 
```

### How to develop a specific functionality for the toolwrapper

TBA (e.g., generate RPM for Red Hat and others)

##Acknowledgements

Part of this work was supported by the European Union in the 7th Framework Program, IST, through the SCAPE project, Contract 270137.
