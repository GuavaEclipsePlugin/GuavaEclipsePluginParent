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

import net.sf.guavaeclipse.AbstractTest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EqualsCreatorTest extends AbstractTest {


  // private CompilationUnitEditor mockEditor(String content, int start, int len) throws Exception {
  // CompilationUnitEditor editor = mock(CompilationUnitEditor.class);
  // IDocumentProvider docProvider = mock(IDocumentProvider.class);
  // FileEditorInput editorInput = mock(FileEditorInput.class);
  // ISelectionProvider selectionProvider = mock(ISelectionProvider.class);
  // IDocument doc = new Document(content);
  //
  // Workspace ws = new Workspace();
  // System.out.println(EqualsCreatorTest.class.getProtectionDomain().getCodeSource().getLocation());
  // IPath path =
  // Path.fromOSString(EqualsCreatorTest.class.getProtectionDomain().getCodeSource()
  // .getLocation()
  // + "/src/main/resources/SampleSimple.java");
  // IFile file = new TestFile(path, ws);
  // when(editor.getEditorInput()).thenReturn(editorInput);
  // when(editorInput.getFile()).thenReturn(file);
  // when(docProvider.getDocument(editorInput)).thenReturn(doc);
  // when(editor.getDocumentProvider()).thenReturn(docProvider);
  // when(editor.getSelectionProvider()).thenReturn(selectionProvider);
  // when(selectionProvider.getSelection()).thenReturn(new TextSelection(start, len));
  // return editor;
  // }

  @Test
  public void test() throws Exception {
    // ArrayList<Object> fields = new ArrayList<Object>(2);
    // MethodInsertionPoint insertionPoint =
    // new MethodInsertionPoint(mockEditor(readFile("Input_SampleSimple.txt"), 0, 0));
    // EqualsCreator ec =
    // new EqualsCreator(insertionPoint, Utils.getNonStaticFieldNames(insertionPoint
    // .getInsertionType()));
    //
    // ec.generate();
  }

  // public String readFile(String fileName) throws IOException, URISyntaxException {
  // BufferedReader br =
  // new BufferedReader(new InputStreamReader(this.getClass().getClassLoader()
  // .getResourceAsStream(fileName)));
  // try {
  // StringBuilder sb = new StringBuilder();
  // String line = br.readLine();
  //
  // while (line != null) {
  // sb.append(line);
  // sb.append("\n");
  // line = br.readLine();
  // }
  // return sb.toString();
  // } finally {
  // br.close();
  // }
  // }
}
