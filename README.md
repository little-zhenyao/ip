# Windy project template

This is a project template for a greenfield Java project. This is a project template for a greenfield Java project named _Windy_. Given below are instructions on how to use it.

## Continuous integration

The [CI workflow](.github/workflows/ci.yml) runs on every push and pull request,
and can also be started manually from GitHub's **Actions** tab once the workflow
is on the default branch. It uses Java 25 to compile, run JUnit tests, check
production and test code with Checkstyle, and build the distributions on Linux,
Windows, and macOS. Console UI test plans and interactive GUI tests are not run by CI.

To run the same checks locally, select JDK 25 and run:

```bash
# Linux and macOS
./gradlew --no-daemon build
```

```powershell
# Windows (PowerShell)
.\gradlew.bat --no-daemon build
```

In the repository's **Actions** tab, open **CI**, select a run, and select a
platform's job to see its results. If a job fails, expand the failed step to
read the error log. All three platform jobs must pass to verify the CI build.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Windy.java` file, right-click it, and choose `Run Windy.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    __        ___           _
    \ \      / (_)_ __   __| |_   _
     \ \ /\ / /| | '_ \ / _` | | | |
      \ V  V / | | | | | (_| | |_| |
       \_/\_/  |_|_| |_|\__,_|\__, |
                              |___/
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
