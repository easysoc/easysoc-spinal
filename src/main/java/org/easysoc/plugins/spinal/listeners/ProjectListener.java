package org.easysoc.plugins.spinal.listeners;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.SystemProperties;
import org.easysoc.plugins.spinal.module.SpinalModuleType;
import org.jetbrains.annotations.NotNull;

public class ProjectListener implements ProjectManagerListener {

  @Override
  public void projectOpened(@NotNull Project project) {
    String myType = (String) project.getUserData(SpinalModuleType.NEWPROJECT_TYPE);
    // new project
    if (myType != null) {
      // should be same as SpinalModuleType's getName() method value
      if (myType.equals("SpinalHDL")) {
        // only listen once for new project, not for old project
//        project.getMessageBus().connect().subscribe(ProjectDataImportListener.TOPIC,new SbtImportListener(project));

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String IS_FIRST_IMPORT = "easysoc.chip.first.import";
        if (propertiesComponent.getBoolean(IS_FIRST_IMPORT,true)) {
          showFirstImportMessage();
          propertiesComponent.setValue(IS_FIRST_IMPORT, "false");
        }
      }
    }
  }

  private void showFirstImportMessage() {
    String ivyHome = SystemProperties.getUserHome() + "/.ivy2\n";

    String message = "You are creating a SpinalHDL project for the first time using the Project Wizard.\n" +
            "If you have never used the sbt build tool, the application will try to resolve\n" +
            "it's dependencies and download all dependent packages to the directory:\n" + ivyHome +
            "The initial dependency resolution may takes some time, please be patient!\n";
//    Messages.showInfoMessage(message, "First Project Import Tips");
    Notifications.Bus.notify(new Notification("SpinalHDL", "First project import tips",
            message, NotificationType.INFORMATION));
  }
}
