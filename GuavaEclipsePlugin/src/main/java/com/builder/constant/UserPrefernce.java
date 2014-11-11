package com.builder.constant;

import com.builder.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

public class UserPrefernce {

	public UserPrefernce() {
	}

	public static MethodGenerationStratergy getMethodGenerationStratergy() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String a = store.getString("guavaPreference");
		if (a == null)
			return null;
		if (a.equals("choice1"))
			return MethodGenerationStratergy.USE_SUPER;
		if (a.equals("choice2"))
			return MethodGenerationStratergy.DONT_USE_SUPER;
		if (a.equals("choice3"))
			return MethodGenerationStratergy.SMART_OPTION;
		else
			return null;
	}
}
