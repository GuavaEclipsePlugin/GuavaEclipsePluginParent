/* Copyright 2014
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.AbstractTest;
import net.sf.guavaeclipse.Activator;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserPreferencesPageTest extends AbstractTest {

	@Test
	public void testDefaultPreferenceStoreMethodGenerationStratergy() {
		assertThat(UserPreferenceUtil.getMethodGenerationStratergy(), is(MethodGenerationStratergy.SMART_OPTION));
	}

	@Test
	public void testChoice1PreferenceStoreMethodGenerationStratergy() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE,
						MethodGenerationStratergy.USE_SUPER.name());
		assertThat(UserPreferenceUtil.getMethodGenerationStratergy(), is(MethodGenerationStratergy.USE_SUPER));
	}

	@Test
	public void testChoice2PreferenceStoreMethodGenerationStratergy() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE,
						MethodGenerationStratergy.DONT_USE_SUPER.name());
		assertThat(UserPreferenceUtil.getMethodGenerationStratergy(), is(MethodGenerationStratergy.DONT_USE_SUPER));
	}

	@Test
	public void testChoice3PreferenceStoreMethodGenerationStratergy() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(UserPreferencePage.SUPERCALL_STRATEGY_PREFERENCE,
						MethodGenerationStratergy.SMART_OPTION.name());
		assertThat(UserPreferenceUtil.getMethodGenerationStratergy(), is(MethodGenerationStratergy.SMART_OPTION));
	}

	@Test
	public void testDefaultEqualsEqualityType() {
		assertThat(UserPreferenceUtil.getEqualsEqualityType(), is(EqualsEqualityType.INSTANCEOF));
	}

	@Test
	public void testCoice1EqualsEqualityType() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE,
						EqualsEqualityType.INSTANCEOF.name());
		assertThat(UserPreferenceUtil.getEqualsEqualityType(), is(EqualsEqualityType.INSTANCEOF));
	}

	@Test
	public void testChoice2EqualsEqualityType() {
		Activator
				.getDefault()
				.getPreferenceStore()
				.setValue(UserPreferencePage.INSTANCEOF_CLASSEQUALS_PREFERENCE,
						EqualsEqualityType.CLASS_EQUALITY.name());
		assertThat(UserPreferenceUtil.getEqualsEqualityType(), is(EqualsEqualityType.CLASS_EQUALITY));
	}

}
