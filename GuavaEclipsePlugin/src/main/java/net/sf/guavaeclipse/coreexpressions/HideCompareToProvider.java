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

import java.util.HashMap;
import java.util.Map;

import net.sf.guavaeclipse.preferences.UserPreferenceUtil;

import org.eclipse.ui.AbstractSourceProvider;

public class HideCompareToProvider extends AbstractSourceProvider {

  public static final String HIDE_COMPARE_TO_VAR = "net.sf.guavaeclipse.hideCompareTo"; //$NON-NLS-1$

  @Override
  public void dispose() {}

  @SuppressWarnings("rawtypes")
  @Override
  public Map getCurrentState() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(HIDE_COMPARE_TO_VAR, UserPreferenceUtil.isHideCompareTo());
    return map;
  }

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] {HIDE_COMPARE_TO_VAR};
  }

}
