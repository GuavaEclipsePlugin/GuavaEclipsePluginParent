package com.builder.preferences;

import net.sf.guavaeclipse.constants.EqualsEqualityType;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.builder.Activator;

public class UserPreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    public UserPreferencePage()
    {
        super(1);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Method generation preferences");
    }

    @Override
    public void createFieldEditors()
    {
		addField(new RadioGroupFieldEditor("guavaPreference", "super method behavior", 1, new String[][] {
            new String[] {
                "Use super class Methods (toString(), equals() and hashCode())", "choice1"
            }, new String[] {
                "Don't use super class Methods (toString(), equals() and hashCode())", "choice2"
            }, new String[] {
                "Use super class Methods (Only if superclass is not \"java.lang.Object\")", "choice3"
            }
        }, getFieldEditorParent()));
		addField(new RadioGroupFieldEditor("guavaEclipseEqualsPreference", "instanceOf or class equality in equals", 1, new String[][] {
		        new String[] { "Use instanceof in equals()", EqualsEqualityType.INSTANCEOF.name() },
		        new String[] { "use class equality", EqualsEqualityType.CLASS_EQUALITY.name() } 
		    }, getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench iworkbench)
    {
    }
}
