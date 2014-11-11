package com.builder.preferences;

import net.sf.guavaeclipse.constants.EqualsEqualityType;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.builder.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{

    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault("guavaPreference", "choice3");
		store.setDefault("guavaEclipseEqualsPreference", EqualsEqualityType.INSTANCEOF.name());
    }
}
