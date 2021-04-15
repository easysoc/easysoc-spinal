package org.easysoc.plugins.spinal.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

public class CreateSpinalComponentAction extends CreateFileFromTemplateAction implements DumbAware {
    public CreateSpinalComponentAction() {
        super("SpinalHDL Component", "Create new SpinalHDL component", Icons.Spinal);
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder
                .setTitle("New SpinalHDL Component")
                .addKind("Component", Icons.Spinal, "SpinalHDL Component");
    }

    @Override
    protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
        return "SpinalHDL Component";
    }
}
