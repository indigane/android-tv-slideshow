# Exploring Java Libraries

When you need to understand how to use a Java library and the documentation is not available or insufficient, you can explore the library's classes and methods using the following steps:

1.  **Find the library's JAR file:**
    The first step is to locate the JAR file for the library. If you are using a build tool like Gradle or Maven, the JAR file will be located in the local cache. You can use the `find` command to locate it. For example, to find the `smbj` library in the Gradle cache, you can use the following command:

    ```bash
    find ~/.gradle -name "*smbj*"
    ```

2.  **Unzip the JAR file:**
    Once you have located the JAR file, you can unzip it to a temporary directory to inspect its contents.

    ```bash
    mkdir -p /tmp/smbj
    unzip /path/to/smbj.jar -d /tmp/smbj
    ```

3.  **Inspect the class files:**
    After unzipping the JAR file, you can use the `javap` command to inspect the class files and get a list of the available methods and fields. For example, to inspect the `DiskShare` class, you can use the following command:

    ```bash
    javap /tmp/smbj/com/hierynomus/smbj/share/DiskShare.class
    ```

This will give you a list of all the methods and fields in the `DiskShare` class, which you can use to understand how to interact with it.
