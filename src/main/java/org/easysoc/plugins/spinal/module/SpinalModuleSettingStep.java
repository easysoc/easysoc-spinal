package org.easysoc.plugins.spinal.module;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectBuilder;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ide.util.projectWizard.WizardInputField;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.platform.templates.ArchivedProjectTemplate;
import com.intellij.platform.templates.SpinalTemplateModuleBuilder;
import com.intellij.platform.templates.TemplateModuleBuilder;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class SpinalModuleSettingStep extends ModuleWizardStep {

  private final String SBT_IMPORT = "sbt.shell.import";
  private final String SBT_BUILD = "sbt.shell.build";
  
  private final PropertiesComponent settings = PropertiesComponent.getInstance();

  // in SpinalModuleSettingStep.form
  private JPanel myPanel;
  private JPanel mySettingsPanel;

  private WizardContext myWizardContext;
  private Properties myProperties;

  private ComboBox comboSbtVersions;
  private ComboBox comboScalaVersions;
  private ComboBox comboSpinalVersions;
  private JCheckBox sbtImport;
  private JCheckBox sbtBuild;
  private JTextField versionField;

  // called when Create New Project
  public SpinalModuleSettingStep(WizardContext context) {
    myWizardContext = context;
  }

  // called when this step show
  @Override
  public void _init() {

    ProjectBuilder projectBuilder = myWizardContext.getProjectBuilder();

    // components only needs to be rebuilt once
    if (sbtImport == null) {
      sbtImport = new JCheckBox("Use Sbt Shell For Import", settings.getBoolean(SBT_IMPORT,false));
      sbtBuild = new JCheckBox("Use Sbt Shell For Build", settings.getBoolean(SBT_BUILD,false));

      String[] scalaVersions = {"2.12.13"};
      comboScalaVersions = new ComboBox(scalaVersions);
      Dimension preferSize = comboScalaVersions.getPreferredSize();

      String[] sbtVersions = {"1.5.5"};
      comboSbtVersions = new ComboBox(sbtVersions);
      comboSbtVersions.setPreferredSize(preferSize);

      String[] spinalVersions = {"1.6.0"};
      comboSpinalVersions = new ComboBox(spinalVersions);
      comboSpinalVersions.setPreferredSize(preferSize);

      versionField = new JTextField("1.0.0");
      versionField.setPreferredSize(preferSize);

      addSettingsComponent(sbtImport);
      addSettingsComponent(sbtBuild);

      addSettingsField("Sbt Version:",comboSbtVersions);
      addSettingsField("Scala Version:",comboScalaVersions);
      addSettingsField("SpinalHDL Version:", comboSpinalVersions);
      addSettingsField("Version:", versionField);
    }
  }

  @Override
  public JComponent getComponent() {
    return myPanel;
  }

  @Override
  public void onWizardFinished() {
    // all components have been destroyed
    try {
      ProjectBuilder projectBuilder = myWizardContext.getProjectBuilder();
      if (projectBuilder instanceof TemplateModuleBuilder) {
        TemplateModuleBuilder builder = (TemplateModuleBuilder)projectBuilder;

        if (builder.getModuleType().getId().equals("SPINAL_MODULE")) {
          Field fieldTemplate = TemplateModuleBuilder.class.getDeclaredField("myTemplate");
          Field fieldAdditionalFields = TemplateModuleBuilder.class.getDeclaredField("myAdditionalFields");

          fieldTemplate.setAccessible(true);
          fieldAdditionalFields.setAccessible(true);

          ArchivedProjectTemplate myTemplate = (ArchivedProjectTemplate)fieldTemplate.get(builder);
          List<WizardInputField<?>> myAdditionalFields = (List<WizardInputField<?>>) fieldAdditionalFields.get(builder);

          myWizardContext.setProjectBuilder(new SpinalTemplateModuleBuilder(myTemplate,builder.getModuleType(),myAdditionalFields,myWizardContext));
        }

      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  // will call this method after click Next button,then call onStepLeaving method
  @Override
  public void updateDataModel() {

    myProperties = new Properties();
    myProperties.setProperty("USE_SBT_IMPORT",String.valueOf(sbtImport.isSelected()));
    myProperties.setProperty("USE_SBT_BUILD",String.valueOf(sbtBuild.isSelected()));
    myProperties.setProperty("SBT_VERSION",comboSbtVersions.getSelectedItem().toString());
    myProperties.setProperty("SCALA_VERSION",comboScalaVersions.getSelectedItem().toString());
    myProperties.setProperty("SPINAL_VERSION", comboSpinalVersions.getSelectedItem().toString());
    myProperties.setProperty("VERSION",versionField.getText());

    myWizardContext.putUserData(SpinalModuleType.EASYSOC_CHIP,myProperties);

    // must be string
    settings.setValue(SBT_IMPORT, String.valueOf(sbtImport.isSelected()));
    settings.setValue(SBT_BUILD, String.valueOf(sbtBuild.isSelected()));
  }

  public JLabel addSettingsField(@NotNull String label, @NotNull JComponent field) {
    return addField(label, field, mySettingsPanel, false);
  }

  public JLabel addSettingsField(@NotNull String label, @NotNull JComponent field, boolean withEnabled) {
    return addField(label, field, mySettingsPanel, withEnabled);
  }

  static JLabel addField(String label, JComponent field, JPanel panel, boolean withEnabled) {
    JLabel jLabel = new JBLabel(label);
    jLabel.setLabelFor(field);
    jLabel.setVerticalAlignment(SwingConstants.TOP);

    // GridBagConstraints https://blog.csdn.net/zxlqaz/article/details/68061101
    panel.add(jLabel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.WEST,
                                             GridBagConstraints.VERTICAL, JBUI.insets(5, 0, 5, 0), 4, 0));
    panel.add(field, new GridBagConstraints(1, GridBagConstraints.RELATIVE, withEnabled?1:2, 1, 0, 0, GridBagConstraints.WEST,
                                            GridBagConstraints.VERTICAL, JBUI.insetsBottom(5), 0, 0));

    if (withEnabled) {
      JCheckBox checkBox = new JCheckBox("Enabled");
      checkBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          field.setEnabled(checkBox.isSelected());
        }
      });
      panel.add(checkBox, new GridBagConstraints(2, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.WEST,
                                              GridBagConstraints.VERTICAL, JBUI.insets(5, 5, 5, 0), 0, 0));
    }
    return jLabel;
  }

  public void addSettingsComponent(@NotNull JComponent component) {
    mySettingsPanel.add(component, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 3, 1, 1.0, 0, GridBagConstraints.NORTHWEST,
                                                GridBagConstraints.HORIZONTAL, JBUI.insetsBottom(5), 0, 0));
  }

}
