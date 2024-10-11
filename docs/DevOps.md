---
layout: page
title: DevOps guide
---

* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## Code Formatting

This project uses the [Java coding standard (basic)
](https://se-education.org/guides/conventions/java/basic.html) and the Google Java style guide. The code style is
enforced using Checkstyle. The checks are run as part of the CI process.

Developers are recommended to import the [`Intellij IDEA code style XML`](/config/IntelliJ.xml) and [
`Eclipse XML Profile`](/config/Eclipse.xml) from the `config` folder to IntelliJ IDEA to ensure that the code style is
consistent across all developers after reformatting. Follow the following steps to import the code style settings:

1. Navigate to `Settings/Preferences` -> `Editor` -> `Code Style` -> `Java`.
2. Beside scheme, click on the gear icon and select `Import Scheme` -> `Intellij IDEA code style XML`.
3. Select the `IntelliJ.xml` file from the `config` folder.
4. Click `OK` to apply the code style.
5. Beside scheme, click on the gear icon and select `Import Scheme` -> `Eclipse XML Profile`.
6. Select the `Eclipse.xml` file from the `config` folder.
7. Check `Current Scheme` and click `OK` to apply the code style.
8. To reformat the code, press `Ctrl + Alt + L` on Windows or `Shift + Cmd + Option + L` on macOS – Ensure the option of
   `Optimise Imports` is selected.
9. If you are prompted with `Settings may be overridden by EditorConfig` in `Settings/Preferences` -> `Editor` ->
   `Code Style`, click `disable`.

\* If you are using a different IDE, you can refer to the respective IDE's documentation on how to import code style
settings. But we recommend using IntelliJ IDEA for this project.

## Build automation

This project uses Gradle for **build automation and dependency management**. **You are recommended to
read [this Gradle Tutorial from the se-edu/guides](https://se-education.org/guides/tutorials/gradle.html)**.

Given below are how to use Gradle for some important project tasks.

* **`clean`**: Deletes the files created during the previous build tasks (e.g. files in the `build` folder).<br>
  e.g. `./gradlew clean`

* **`shadowJar`**: Uses the ShadowJar plugin to creat a fat JAR file in the `build/lib` folder, *if the current file is
  outdated*.<br>
  e.g. `./gradlew shadowJar`.

* **`run`**: Builds and runs the application.<br>
  **`runShadow`**: Builds the application as a fat JAR, and then runs it.

* **`checkstyleMain`**: Runs the code style check for the main code base.<br>
  **`checkstyleTest`**: Runs the code style check for the test code base.

* **`test`**: Runs all tests.
    * `./gradlew test`— Runs all tests
    * `./gradlew clean test`— Cleans the project and runs tests

--------------------------------------------------------------------------------------------------------------------

## Continuous integration (CI)

This project uses GitHub Actions for CI. The project comes with the necessary GitHub Actions configurations files (in
the `.github/workflows` folder). No further setting up required.

### Code coverage

As part of CI, this project uses Codecov to generate coverage reports. When CI runs, it will generate code coverage
data (based on the tests run by CI) and upload that data to the CodeCov website, which in turn can provide you more info
about the coverage of your tests.

However, because Codecov is known to run into intermittent problems (e.g., report upload fails) due to issues on the
Codecov service side, the CI is configured to pass even if the Codecov task failed. Therefore, developers are advised to
check the code coverage levels periodically and take corrective actions if the coverage level falls below desired
levels.

To enable Codecov for forks of this project, follow the steps given
in [this se-edu guide](https://se-education.org/guides/tutorials/codecov.html).

### Repository-wide checks

In addition to running Gradle checks, CI includes some repository-wide checks. Unlike the Gradle checks which only cover
files used in the build process, these repository-wide checks cover all files in the repository. They check for
repository rules which are hard to enforce on development machines such as line ending requirements.

These checks are implemented as POSIX shell scripts, and thus can only be run on POSIX-compliant operating systems such
as macOS and Linux. To run all checks locally on these operating systems, execute the following in the repository root
directory:

`./config/travis/run-checks.sh`

Any warnings or errors will be printed out to the console.

**If adding new checks:**

* Checks are implemented as executable `check-*` scripts within the `.github` directory. The `run-checks.sh` script will
  automatically pick up and run files named as such. That is, you can add more such files if you need and the CI will do
  the rest.

* Check scripts should print out errors in the format `SEVERITY:FILENAME:LINE: MESSAGE`
    * SEVERITY is either ERROR or WARN.
    * FILENAME is the path to the file relative to the current directory.
    * LINE is the line of the file where the error occurred and MESSAGE is the message explaining the error.

* Check scripts must exit with a non-zero exit code if any errors occur.

--------------------------------------------------------------------------------------------------------------------

## Making a release

Here are the steps to create a new release.

1. Update the version number in [
   `MainApp.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java).
2. Generate a fat JAR file using Gradle (i.e., `gradlew shadowJar`).
3. Tag the repo with the version number. e.g. `v0.1`
4. [Create a new release using GitHub](https://help.github.com/articles/creating-releases/). Upload the JAR file you
   created.
