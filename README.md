## SpinalHDL plugin for Intellij platform

### Features

- Create new SpinalHDL project based on ProjectWizard and Templates

![](https://plugins.jetbrains.com/files/16439/screenshot_b0f5aba5-6605-4cb0-9f1e-fba0c6181500)

### Build

1. [learn](https://www.jetbrains.org/intellij/sdk/docs/basics/basics.html) how to develop an intellij plugin with Gradle

2. modify build.gradle configuration according to your environment

    change `ideDirectory`  to "your idea install path"

    comment or remove `jvmArgs '-Didea.platform.prefix=Chip'`

3. run gradle tasks buildPlugin or runIde directly