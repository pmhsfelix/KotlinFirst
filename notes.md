# Notes

Pre-conditions:
* JDK installed
* Gradle installed (e.g. use `brew install gradle` to install it on OSX)

Make yourself confortable, create an empty directory and move into it.
We will start by creating a reproducible gradle based build by using the [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)

```
gradle wrapper
```

This command creates a shell script, named `gradlew`, that is responsible for downloading the correct version of gradle and by starting it.

The next step is to create the `build.gradle` file.
For that, we are using the structure described in the [kotlin help](http://kotlinlang.org/docs/reference/using-gradle.html)

```
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:0.12.613'
  }
}

apply plugin: "kotlin"

repositories {
  mavenCentral()
}

dependencies {
  compile 'org.jetbrains.kotlin:kotlin-stdlib:0.12.613'
}
```

Notice that this build file uses the special `kotlin` gradle plugin, which defines the task require to compile Kotlin files.
Notice also that the plugin is being provided via Maven.

With this `build.gradle` file define, we can do a first build.
And yes, we are aware that no kotlin source file was defined yet.
Just keep with us while we prepare all the build environment.

* After running `./gradlew build` we get the following directory tree

```
.
├── LICENSE
├── README.md
├── build
│   ├── libs
│   │   └── KotlinFirst.jar
│   └── tmp
│       └── jar
│           └── MANIFEST.MF
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

This means that all the build process is in place.
As expected, the `KotlinFirst.jar` does have anything there, except for the manifest, since no source files exist.

```
$ jar tf build/libs/KotlinFirst.jar
META-INF/
META-INF/MANIFEST.MF
```

So, lets start creating some kotlin source files.
First, create the source folder at `src/main/kotlin`.
Notice that this folder name is similar to the `src/main/java` typically used in java projects, namely the ones based on Maven or Gradle, with the `java` replaced by `kotlin`.
Then, create the `KotlinFirst.kt source file at that location, with the following contents

```
package pmhsfelix.kotlinfirst

fun main(args:Array<String>) {
    println("Hello world, from kotlin");
}
```



The first line defines the [package](http://kotlinlang.org/docs/reference/packages.html). 
Notice that there isn't a required mapping between package names and directories: the package name is `pmhsfelix.kotlinfirst` but it is place in the root source directory.

 The remaining lines define the entry point function (yes, Kotlin can have functions at the [package level](http://kotlinlang.org/docs/reference/functions.html)).

After building the project, the resulting JAR now has some classes on it.

```
$ jar tf build/libs/KotlinFirst.jar
META-INF/
META-INF/MANIFEST.MF
pmhsfelix/
pmhsfelix/kotlinfirst/
pmhsfelix/kotlinfirst/KotlinfirstPackage$KotlinFirst$79ace982.class
pmhsfelix/kotlinfirst/KotlinfirstPackage.class
```

The `KotlinfirstPackage.class` appears to be the class created to host the package scope members, namely `main` function.

We can try to star this application by doing

```
java -cp build/libs/KotlinFirst.jar pmhsfelix.kotlinfirst.KotlinfirstPackage
```

However, an error occurs

```
Exception in thread "main" java.lang.NoClassDefFoundError: kotlin/jvm/internal/Reflection
    at pmhsfelix.kotlinfirst.KotlinfirstPackage.<clinit>(KotlinFirst.kt)
Caused by: java.lang.ClassNotFoundException: kotlin.jvm.internal.Reflection
    at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
    at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
    at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:331)
    at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
    ... 1 more

```
The reason is that even a minimal Kotlin program requires the Kotlin standard class library, which isn't being provided on the class path.

* Add the application plugin to build.gradle and define the main class Name

apply plugin: "kotlin"
apply plugin: "application"
(...)
mainClassName = "pmhsfelix.kotlinfirst.KotlinfirstPackage"

* `./gradlew installApp`

$tree 
build/install
└── KotlinFirst
    ├── bin
    │   ├── KotlinFirst
    │   └── KotlinFirst.bat
    └── lib
        ├── KotlinFirst.jar
        ├── kotlin-runtime-0.12.613.jar
        └── kotlin-stdlib-0.12.613.jar




