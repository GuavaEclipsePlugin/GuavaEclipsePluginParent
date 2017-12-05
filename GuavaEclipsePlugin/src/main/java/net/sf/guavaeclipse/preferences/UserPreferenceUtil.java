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

import static net.sf.guavaeclipse.preferences.CompareToCommentsType.EVERY_FIELD_COMMENT;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.COMPARE_COMMENT_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.EQUALS_HASHCODE_PRIMITIVESBOXING;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.FIELDS_GETTER_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.HASH_CODE_STRATEGY_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.HIDE_COMPARE_TO_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.JAVA_UTILS_OBJECTS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.MORE_OBJECTS_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.NON_NLS_1_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE;

import org.eclipse.jface.preference.IPreferenceStore;

import net.sf.guavaeclipse.Activator;

public final class UserPreferenceUtil {

  private UserPreferenceUtil() {
    // private default constructor for util class
  }

  public static MethodGenerationStratergy getMethodGenerationStratergy() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(SUPERCALL_STRATEGY_PREFERENCE);
    return MethodGenerationStratergy.valueOf(a);
  }

  public static EqualsEqualityType getEqualsEqualityType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(INSTANCEOF_CLASSEQUALS_PREFERENCE);
    return EqualsEqualityType.valueOf(a);
  }

  public static FieldsGetterType getFieldsOrGetterType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(FIELDS_GETTER_PREFERENCE);
    return FieldsGetterType.valueOf(a);
  }

  public static HashCodeStrategyType getHashCodeStrategyType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(HASH_CODE_STRATEGY_PREFERENCE);
    return HashCodeStrategyType.valueOf(a);
  }


  public static Boolean isHideCompareTo() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(HIDE_COMPARE_TO_PREFERENCE);
    return Boolean.valueOf(a);
  }

  public static Boolean useMoreObjects() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(MORE_OBJECTS_PREFERENCE);
    return Boolean.valueOf(a);
  }

  public static CompareToCommentsType getCompareToCommentsType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(COMPARE_COMMENT_PREFERENCE);
    if (a == null || a.trim().isEmpty()) {
      return EVERY_FIELD_COMMENT;
    }
    return CompareToCommentsType.valueOf(a);
  }

  public static String getCompareToTaskTag() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(UserPreferencePage.COMPARE_COMMENT_TASK_TAG);
    if (a == null) {
      return "";
    }
    return a;
  }
  
  public static Boolean useJavaUtilsObjects() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(JAVA_UTILS_OBJECTS_PREFERENCE);
    return Boolean.valueOf(a);
  }
  
  public static Boolean usePrimitivesCompareInEquals() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE);
    return Boolean.valueOf(a);
  }
  
  public static PrimitivsBoxingType getPrimitivsBoxingType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(EQUALS_HASHCODE_PRIMITIVESBOXING);
    return PrimitivsBoxingType.valueOf(a);
  }

  public static Boolean isNonNls1PreferenceSelected() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(NON_NLS_1_PREFERENCE);
    return Boolean.valueOf(a);
  }
}
