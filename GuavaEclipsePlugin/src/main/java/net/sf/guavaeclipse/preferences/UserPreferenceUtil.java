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

import org.eclipse.jface.preference.IPreferenceStore;

public final class UserPreferenceUtil {

  private UserPreferenceUtil() {
    // private default constructor for util class
  }

  public static MethodGenerationStratergy getMethodGenerationStratergy() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE);
    return MethodGenerationStratergy.valueOf(a);
  }

  public static EqualsEqualityType getEqualsEqualityType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE);
    return EqualsEqualityType.valueOf(a);
  }

  public static FieldsGetterType getFieldsOrGetterType() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    String a = store.getString(UserPreferencePage.FIELDS_GETTER_PREFERENCE);
    return FieldsGetterType.valueOf(a);
  }
}
