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
package net.sf.guavaeclipse.creator;

import java.util.List;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.exception.MehodGenerationFailedException;
import net.sf.guavaeclipse.preferences.EqualsEqualityType;
import net.sf.guavaeclipse.preferences.FieldsGetterType;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.jdt.core.JavaModelException;

public abstract class AbstractEqualsHashCodeMethodCreator extends AbstractMethodCreator {

  protected final EqualsEqualityType eet;
  protected final FieldsGetterType fgt;

  public AbstractEqualsHashCodeMethodCreator(MethodInsertionPoint insertionPoint,
      List<String> fields) throws JavaModelException {
    super(insertionPoint, fields);
    this.eet = UserPreferenceUtil.getEqualsEqualityType();
    this.fgt = UserPreferenceUtil.getFieldsOrGetterType();
  }

  protected String getGetterOrField(String fieldName) {
    if (fgt == FieldsGetterType.GETTER) {
      try {
        String getterMethod = getGetterMethodName(fieldName);
        List<String> methodNames = Utils.getMethodNames(insertionPoint.getInsertionType());
        for (String methodName : methodNames) {
          if (methodName.equals(getterMethod)) {
            return methodName + "()";
          }
        }
      } catch (JavaModelException e) {
        throw new MehodGenerationFailedException(e.getMessage());
      }
    }
    return fieldName;
  }

  private String getGetterMethodName(String field) {
    StringBuilder bu = new StringBuilder("get");
    bu.append(field.substring(0, 1).toUpperCase());
    bu.append(field.substring(1, field.length()));
    return bu.toString();
  }
}
