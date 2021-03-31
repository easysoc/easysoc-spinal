package org.easysoc.plugins.spinal.module;

import com.intellij.ide.projectWizard.NewProjectWizard;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

public class SpinalModuleBuilder extends ModuleBuilder {

  private SettingsStep mySettingsStep;

  @Override
  public ModuleType getModuleType() {
    return SpinalModuleType.getInstance();
  }

  @Override
  public int getWeight() {
    return 998;
  }

  @Override
  public boolean isTemplateBased() {
    return false;
  }

  @Override
  public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep) {
    // ProjectWizardStepFactory need java plugin
    mySettingsStep = settingsStep;
    ModuleWizardStep step = ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, this,
            this::isSuitableSdkType);
    return step;
  }

  @Override
  public boolean isSuitableSdkType(SdkTypeId sdk) {
    return sdk == JavaSdk.getInstance();
  }

  // fix setTemplatesList in ProjectTypeStep
  @Override
  public @Nullable ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
    if (context.isCreatingNewProject()) {
      SpinalTemplateWizardStep step = null;
      try {
        step = new SpinalTemplateWizardStep(context,mySettingsStep);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Disposer.register(parentDisposable, step);
      return step;
    } else {

      return new ModuleWizardStep() {
        // 不会执行这里的 _init 和 onStepLeaving 方法

        @Override
        public JComponent getComponent() {
          JLabel label = new JLabel("Does not support create SpinalHDL module!");
          label.setHorizontalAlignment(SwingConstants.CENTER);
          return label;
        }

        // will call this method after click Next button
        @Override
        public void updateDataModel() {
          NewProjectWizard myWizard = (NewProjectWizard)context.getWizard();
          myWizard.doCancelAction();
        }
      };
    }
  }

  @Override
  public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
    if (wizardContext.isCreatingNewProject()) {
      return new ModuleWizardStep[]{new SpinalModuleSettingStep(wizardContext)};
    } else {
      return new ModuleWizardStep[0];
    }
  }
}
