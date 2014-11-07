package com.builder.preferences;

import com.builder.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class UserPreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    public UserPreferencePage()
    {
        super(1);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Method generation preferences");
    }

    public void createFieldEditors()
    {
        addField(new RadioGroupFieldEditor("guavaPreference", "", 1, new String[][] {
            new String[] {
                "Use super class Methods (toString(), equals() and hashCode())", "choice1"
            }, new String[] {
                "Don't use super class Methods (toString(), equals() and hashCode())", "choice2"
            }, new String[] {
                "Use super class Methods (Only if superclass is not \"java.lang.Object\")", "choice3"
            }
        }, getFieldEditorParent()));
    }

    public void init(IWorkbench iworkbench)
    {
    }
}
