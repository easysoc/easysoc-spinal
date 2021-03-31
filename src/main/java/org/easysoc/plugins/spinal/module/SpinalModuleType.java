package org.easysoc.plugins.spinal.module;

import com.intellij.openapi.module.*;
import com.intellij.openapi.util.Key;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class SpinalModuleType extends ModuleType<SpinalModuleBuilder> {
  public static final String ID = "SPINAL_MODULE";
  public static final Key EASYSOC_CHIP = Key.create("EASYSOC_CHIP");
  public static final Key NEWPROJECT_TYPE = Key.create("NEWPROJECT_TYPE");

  public SpinalModuleType() {
    super(ID);
  }

  public static SpinalModuleType getInstance() {
    return (SpinalModuleType) ModuleTypeManager.getInstance().findByID(ID);
  }

  @NotNull
  @Override
  public SpinalModuleBuilder createModuleBuilder() {
    return new SpinalModuleBuilder();
  }

  @NotNull
  @Override
  public String getName() {
    return "SpinalHDL";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Create new SpinalHDL project based on templates.";
  }


  @NotNull
  @Override
  public Icon getNodeIcon(@Deprecated boolean b) {
    return Icons.Spinal;
  }

}
