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
import static net.sf.guavaeclipse.preferences.CompareToCommentsType.NO_COMMENTS;
import static net.sf.guavaeclipse.preferences.CompareToCommentsType.ONLY_ONE_COMMENT;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.COMPARE_COMMENT_PREFERENCE;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.COMPARE_COMMENT_TASK_TAG;
import static net.sf.guavaeclipse.preferences.UserPreferencePage.HIDE_COMPARE_TO_PREFERENCE;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import net.sf.guavaeclipse.Activator;

public class CompareToPreferencePage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

  public CompareToPreferencePage() {
    super(FieldEditorPreferencePage.GRID);
  }

  @Override
  public void init(IWorkbench arg0) {
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("CompareTo Method generation preferences");
  }

  @Override
  protected void createFieldEditors() {
    addField(new BooleanFieldEditor(HIDE_COMPARE_TO_PREFERENCE, "Hide the compareTo menu",
        getFieldEditorParent()));
    
    addField(new RadioGroupFieldEditor(COMPARE_COMMENT_PREFERENCE,
        "Kind of \"advice\" comments in compareTo method for non-comparable fields", 1,
        new String[][] {
            new String[] {"for every non-comparable field a seperate comment",
                EVERY_FIELD_COMMENT.name()},
            new String[] {"only one comment at beginning of method",
                ONLY_ONE_COMMENT.name()},
            new String[] {"no comments at all (not recommended)",
                NO_COMMENTS.name()}}, getFieldEditorParent(), true));
    
    StringFieldEditor sfe = new StringFieldEditor(COMPARE_COMMENT_TASK_TAG, "Task Tag prefix for compareTo comments", getFieldEditorParent());
    sfe.setEmptyStringAllowed(true);
    addField(sfe);
  }
  
}
