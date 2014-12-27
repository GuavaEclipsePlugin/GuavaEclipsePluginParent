/*
 * Copyright 2014
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.guavaeclipse.preferences;

import net.sf.guavaeclipse.Activator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class UserPreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {

  public static final String SUPERCALL_STRATEGY_PREFERENCE = "guavaPreference"; //$NON-NLS-1$

  public static final String INSTANCEOF_CLASSEQUALS_PREFERENCE = "guavaEclipseEqualsPreference"; //$NON-NLS-1$

  public static final String FIELDS_GETTER_PREFERENCE = "guavaEclipseFieldsGetterPreference"; //$NON-NLS-1$

  public UserPreferencePage() {
    super(FieldEditorPreferencePage.GRID);
  }

  @Override
  public void createFieldEditors() {
    addField(new RadioGroupFieldEditor(SUPERCALL_STRATEGY_PREFERENCE, "super method behavior", 1,
        new String[][] {
            new String[] {"Use super class Methods (toString(), equals() and hashCode())",
                MethodGenerationStratergy.USE_SUPER.name()},
            new String[] {"Don't use super class Methods (toString(), equals() and hashCode())",
                MethodGenerationStratergy.DONT_USE_SUPER.name()},
            new String[] {
                "Use super class Methods (Only if superclass is not \"java.lang.Object\")",
                MethodGenerationStratergy.SMART_OPTION.name()}}, getFieldEditorParent(), true));
    addField(new RadioGroupFieldEditor(INSTANCEOF_CLASSEQUALS_PREFERENCE,
        "instanceOf or class equality in equals", 1, new String[][] {
            new String[] {"Use instanceof in equals()", EqualsEqualityType.INSTANCEOF.name()},
            new String[] {"use class equality", EqualsEqualityType.CLASS_EQUALITY.name()}},
        getFieldEditorParent(), true));
    addField(new RadioGroupFieldEditor(FIELDS_GETTER_PREFERENCE,
        "use fields directly or use getter methods in equals and hashCode", 1, new String[][] {
            new String[] {"use fields", FieldsGetterType.FIELDS.name()},
            new String[] {"use getter methods", FieldsGetterType.GETTER.name()}},
        getFieldEditorParent(), true));
  }

  @Override
  public void init(IWorkbench iworkbench) {
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Method generation preferences");
  }
}
