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
package net.sf.guavaeclipse.coreexpressions;

import net.sf.guavaeclipse.preferences.UserPreferenceUtil;

import org.eclipse.core.expressions.PropertyTester;

public class HideCompareToPropertyTester extends PropertyTester {

  public static final String HIDE_COMPARE_TO_VAR = "hideCompareTo"; //$NON-NLS-1$
  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    // currently not necessary
    // if (HIDE_COMPARE_TO_VAR.equals(property)) {
    // return !UserPreferenceUtil.isHideCompareTo();
    // }
    return !UserPreferenceUtil.isHideCompareTo().booleanValue();
  }

}
