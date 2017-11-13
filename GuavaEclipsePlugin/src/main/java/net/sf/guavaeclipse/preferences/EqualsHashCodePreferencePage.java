/* Copyright 2015
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.guavaeclipse.preferences;

import static net.sf.guavaeclipse.preferences.EqualsEqualityType.CLASS_EQUALITY;
import static net.sf.guavaeclipse.preferences.EqualsEqualityType.INSTANCEOF;
import static net.sf.guavaeclipse.preferences.FieldsGetterType.FIELDS;
import static net.sf.guavaeclipse.preferences.FieldsGetterType.GETTER;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.FIELDS_GETTER_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.JAVA_UTILS_OBJECTS_PREFERENCE;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import net.sf.guavaeclipse.Activator;

public class EqualsHashCodePreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {


  public EqualsHashCodePreferencePage() {
    super(FieldEditorPreferencePage.GRID);
  }
  
  @Override
  public void init(IWorkbench arg0) {
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("EqualsMethod and HashCodeMethod generation preferences");
  }

  @Override
  protected void createFieldEditors() {
    addField(new BooleanFieldEditor(JAVA_UTILS_OBJECTS_PREFERENCE,
        "Use java.util.Objects in equals/hashCode Methods (requires JDK 1.7 or higher)", getFieldEditorParent()));

    addField(new BooleanFieldEditor(EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE,
        "For primitives don't use Objects method in equals() to avoid casting", getFieldEditorParent()));

    addField(new RadioGroupFieldEditor(INSTANCEOF_CLASSEQUALS_PREFERENCE,
        "instanceOf or class equality in equals", 1, new String[][] {
            new String[] {"Use instanceof in equals()", INSTANCEOF.name()},
            new String[] {"use class equality", CLASS_EQUALITY.name()}}, getFieldEditorParent(),
        true));

    addField(new RadioGroupFieldEditor(FIELDS_GETTER_PREFERENCE,
        "use fields directly or use getter methods in equals and hashCode", 1, new String[][] {
            new String[] {"use fields", FIELDS.name()},
            new String[] {"use getter methods", GETTER.name()}}, getFieldEditorParent(), true));
  }

}
