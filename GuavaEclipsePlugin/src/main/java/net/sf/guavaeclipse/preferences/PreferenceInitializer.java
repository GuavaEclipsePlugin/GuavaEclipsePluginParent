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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static net.sf.guavaeclipse.preferences.CompareToCommentsType.EVERY_FIELD_COMMENT;
import static net.sf.guavaeclipse.preferences.EqualsEqualityType.INSTANCEOF;
import static net.sf.guavaeclipse.preferences.FieldsGetterType.FIELDS;
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.SMART_HASH_CODE;
import static net.sf.guavaeclipse.preferences.MethodGenerationStratergy.SMART_OPTION;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.COMPARE_COMMENT_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.COMPARE_COMMENT_TASK_TAG;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.FIELDS_GETTER_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.HASH_CODE_STRATEGY_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.HIDE_COMPARE_TO_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.JAVA_UTILS_OBJECTS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.MORE_OBJECTS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import net.sf.guavaeclipse.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(SUPERCALL_STRATEGY_PREFERENCE, SMART_OPTION.name());
    store.setDefault(INSTANCEOF_CLASSEQUALS_PREFERENCE, INSTANCEOF.name());
    store.setDefault(FIELDS_GETTER_PREFERENCE, FIELDS.name());
    store.setDefault(HASH_CODE_STRATEGY_PREFERENCE, SMART_HASH_CODE.name());
    store.setDefault(HIDE_COMPARE_TO_PREFERENCE, FALSE.toString());
    store.setDefault(MORE_OBJECTS_PREFERENCE, TRUE.toString());
    store.setDefault(COMPARE_COMMENT_PREFERENCE, EVERY_FIELD_COMMENT.toString());
    store.setDefault(COMPARE_COMMENT_TASK_TAG, "XXX");
    store.setDefault(JAVA_UTILS_OBJECTS_PREFERENCE, TRUE.toString());
    store.setDefault(EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE, TRUE.toString());
  }
}
