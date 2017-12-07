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

import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.ARRAYS_DEEP_HASH_CODE;
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.OBJECTS_HASH_CODE;
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.SMART_HASH_CODE;
import static net.sf.guavaeclipse.preferences.MethodGenerationStratergy.DONT_USE_SUPER;
import static net.sf.guavaeclipse.preferences.MethodGenerationStratergy.SMART_OPTION;
import static net.sf.guavaeclipse.preferences.MethodGenerationStratergy.USE_SUPER;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import net.sf.guavaeclipse.Activator;

public class UserPreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {

  public static final String SUPERCALL_STRATEGY_PREFERENCE = "guavaPreference"; //$NON-NLS-1$

  public static final String FIELDS_GETTER_PREFERENCE = "guavaEclipseFieldsGetterPreference"; //$NON-NLS-1$

  public static final String HIDE_COMPARE_TO_PREFERENCE = "guavaEclipsePlugin.hideCompareTo"; //$NON-NLS-1$

  public static final String MORE_OBJECTS_PREFERENCE = "guavaEclipsePlugin.moreObjects"; //$NON-NLS-1$

  public static final String COMPARE_COMMENT_PREFERENCE = "guavaEclipsePlugin.compareToComments"; //$NON-NLS-1$

  public static final String COMPARE_COMMENT_TASK_TAG = "guavaEclipsePlugin.compareToTaskTag"; //$NON-NLS-1$

  public static final String INSTANCEOF_CLASSEQUALS_PREFERENCE = "guavaEclipseEqualsPreference"; //$NON-NLS-1$

  public static final String JAVA_UTILS_OBJECTS_PREFERENCE = "guavaEclipsePlugin.javaUtilsObjects"; //$NON-NLS-1$

  public static final String HASH_CODE_STRATEGY_PREFERENCE = "guavaEclipsePlugin.HashCodeStrategy"; //$NON-NLS-1$

  public static final String EQUALS_METHOD_COMPARE_PRIMITIVES_PREFERENCE = "guavaEclipsePlugin.equalsMethodComparePrimitivesDirectly"; //$NON-NLS-1$

  public static final String EQUALS_HASHCODE_PRIMITIVESBOXING = "guavaEclipsePlugin.equalsHashCodePrimitivsBoxing"; //$NON-NLS-1$
  
  public static final String TO_STRING_SKIP_NULL_VALUES = "guavaEclipsePlugin.toStringSkipNullValues"; //$NON-NLS-1$

  public static final String NON_NLS_1_PREFERENCE = "guavaEclipsePlugin.NonNls1Preference"; //$NON-NLS-1$

  public static final String CODE_ANALYZE_PREFERENCE = "guavaEclipsePlugin.CodeAnalyzePreference"; //$NON-NLS-1$

  public static final String CODE_ANALYZE_COMMENT_PREFERENCE = "guavaEclipsePlugin.CodeAnalyzeCommentPreference"; //$NON-NLS-1$

  private Button doNothingClickButton;

  private Button generatedAnnotationClickButton;

  private Button codeAnalyseCommentClickButton;

  private CodeAnalysisType codeAnalysisType;
  
  private StringFieldEditor codeAnalysisComment;
  
  public UserPreferencePage() {
    super(FieldEditorPreferencePage.GRID);
  }

  @Override
  public void createFieldEditors() {

    addField(new BooleanFieldEditor(MORE_OBJECTS_PREFERENCE,
        "Use MoreObjects in toString Method (requires guava 18.0)", getFieldEditorParent()));
    addField(new RadioGroupFieldEditor(SUPERCALL_STRATEGY_PREFERENCE, "super method behavior", 1,
        new String[][] {
            new String[] {"Use super class Methods (toString(), equals() and hashCode())",
                USE_SUPER.name()},
            new String[] {"Don't use super class Methods (toString(), equals() and hashCode())",
                DONT_USE_SUPER.name()},
            new String[] {
                "Use super class Methods (Only if superclass is not \"java.lang.Object\")",
                SMART_OPTION.name()}}, getFieldEditorParent(), true));
    addField(new RadioGroupFieldEditor(HASH_CODE_STRATEGY_PREFERENCE,
        "Use java.util.Arrays.deep Utility Methods or com.google.common.base.Objects Methods", 1,
        new String[][] {
            new String[] {"use always com.google.common.base.Objects Utility Methods",
                OBJECTS_HASH_CODE.name()},
            new String[] {"use always java.util.Arrays.deep Utility Methods",
                ARRAYS_DEEP_HASH_CODE.name()},
            new String[] {"Use java.util.Arrays.deep Utility methods only when necessary",
                SMART_HASH_CODE.name()}}, getFieldEditorParent(), true));

    addField(new BooleanFieldEditor(TO_STRING_SKIP_NULL_VALUES,
        "skip NullValues in toString()", getFieldEditorParent()));

    addField(new RadioGroupFieldEditor(NON_NLS_1_PREFERENCE,
        "How to handle NLS1 warnings?", 1,
        new String[][] {
            new String[] {"do nothing about it",
                NonNlsType.NON_NLS_1_DO_NOTHING.name()},
            new String[] {"add $NON-NLS-1$ comment after each toString field",
                NonNlsType.NON_NLS_1_COMMENT.name()},
            new String[] {"add @SuppressWarnings(\"nls\")",
                NonNlsType.NON_NLS_1_SUPRESS.name()}}, getFieldEditorParent(), true));

    createCodeAnalysisGroup(getFieldEditorParent());
//    addField(new RadioGroupFieldEditor(CODE_ANALYZE_PREFERENCE,
//        "Code analysis", 1,
//        new String[][] {
//            new String[] {"add generated annotation",
//                NonNlsType.NON_NLS_1_DO_NOTHING.name()},
//            new String[] {"add comment on same line from method declaration", 
//                NonNlsType.NON_NLS_1_COMMENT.name()},
//            }, getFieldEditorParent(), true));
  }

  private void createCodeAnalysisGroup(Composite composite) {
    codeAnalysisType = UserPreferenceUtil.getCodeAnalysisPreference();
    
    final Group buttonComposite = new Group(composite, SWT.NONE);
    buttonComposite.setFont(composite.getFont());
    GridLayout layout = new GridLayout();
    layout.horizontalSpacing = 8;
    layout.numColumns = 2;
    buttonComposite.setLayout(layout);
    GridData gd = new GridData(GridData.FILL_VERTICAL);
    buttonComposite.setLayoutData(gd);
//        GridData data = new GridData(SWT.LEFT, SWT.HORIZONTAL, true, true);
//    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
//        | GridData.GRAB_HORIZONTAL);
//
//    buttonComposite.setLayoutData(data);
    buttonComposite.setText("Code analysis behaviour");
    
    new PreferenceSpacer(buttonComposite);
    
    String label = "do nothing special for code analysis";
    doNothingClickButton = createRadioButton(buttonComposite, label);
    doNothingClickButton.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            selectClickMode(CodeAnalysisType.NO_SPECIAL_HANDLING, buttonComposite);
            UserPreferenceUtil.saveCodeAnalyzePreference(CodeAnalysisType.NO_SPECIAL_HANDLING);
        }
    });
    doNothingClickButton.setSelection(codeAnalysisType == CodeAnalysisType.NO_SPECIAL_HANDLING);

    new PreferenceSpacer(buttonComposite);
    
    label = "add @Generated annotation";
    generatedAnnotationClickButton = createRadioButton(buttonComposite, label);
    generatedAnnotationClickButton.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            selectClickMode(CodeAnalysisType.ADD_GENERATED_ANNOTATION, buttonComposite);
            UserPreferenceUtil.saveCodeAnalyzePreference(CodeAnalysisType.ADD_GENERATED_ANNOTATION);
        }
    });
    generatedAnnotationClickButton.setSelection(codeAnalysisType == CodeAnalysisType.ADD_GENERATED_ANNOTATION);

    new PreferenceSpacer(buttonComposite);

    label = "add code analyse comment";
    codeAnalyseCommentClickButton = createRadioButton(buttonComposite, label);
    codeAnalyseCommentClickButton.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            selectClickMode(CodeAnalysisType.ADD_COMMENT, buttonComposite);
            UserPreferenceUtil.saveCodeAnalyzePreference(CodeAnalysisType.ADD_COMMENT);
        }
    });
    codeAnalyseCommentClickButton.setSelection(codeAnalysisType == CodeAnalysisType.ADD_COMMENT);
    
    codeAnalysisComment = new StringFieldEditor(CODE_ANALYZE_COMMENT_PREFERENCE, "", buttonComposite);
    codeAnalysisComment.setEnabled(codeAnalysisType == CodeAnalysisType.ADD_COMMENT, buttonComposite);
    codeAnalysisComment.setStringValue(UserPreferenceUtil.getCodeAnalysisCommentPreference());
    addField(codeAnalysisComment);
  }

  private void selectClickMode(CodeAnalysisType codeAnalysisType, Composite composite) {
    this.codeAnalysisType = codeAnalysisType;
    this.codeAnalysisComment.setEnabled(codeAnalysisType == CodeAnalysisType.ADD_COMMENT, composite);
}

  private static Button createRadioButton(Composite parent, String label) {
    Button button = new Button(parent, SWT.RADIO | SWT.LEFT | SWT.HORIZONTAL);
    button.setText(label);
    return button;
}

  @Override
  public void init(IWorkbench iworkbench) {
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Method generation preferences");
  }

}
